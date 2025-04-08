package com.scheduler.orderservice.order.payment.kakao.service;

import com.scheduler.orderservice.order.client.MemberServiceClient;
import com.scheduler.orderservice.order.common.component.KakaoProperties;
import com.scheduler.orderservice.order.common.component.RedisOrderCache;
import com.scheduler.orderservice.order.common.domain.OrderCategory;
import com.scheduler.orderservice.order.common.domain.OrderType;
import com.scheduler.orderservice.order.common.dto.DirectOrderDto;
import com.scheduler.orderservice.order.common.dto.KakaoDto;
import com.scheduler.orderservice.order.payment.event.cancel.CancelOrderEvent;
import com.scheduler.orderservice.order.payment.event.direct.vendor.KakaoAfterDirectOrderEvent;
import com.scheduler.orderservice.order.payment.event.direct.vendor.KakaoDirectOrderEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static com.scheduler.orderservice.order.client.dto.MemberFeignDto.StudentResponse;
import static com.scheduler.orderservice.order.client.dto.OrderDto.CancelOrderInfoResponse;
import static com.scheduler.orderservice.order.common.domain.OrderType.DIRECT;
import static com.scheduler.orderservice.order.payment.kakao.dto.KakaoCancelOrderDto.*;
import static com.scheduler.orderservice.order.payment.kakao.dto.KakaoCancelOrderDto.CancelOrderPreRequest.SingleEbookCancelOrder;
import static com.scheduler.orderservice.order.payment.kakao.dto.KakaoPayRequest.*;
import static com.scheduler.orderservice.order.payment.kakao.dto.KakaoSearchOrderDto.KakaoEbookSearchOrderResponse;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoOrderServiceImpl implements KakaoOrderService {

    private final MemberServiceClient memberServiceClient;

    private final WebClient webClient;
    private final RedisOrderCache redisOrderCache;
    private final KakaoProperties kakaoProperties;
    private final ApplicationEventPublisher eventPublisher;


    @Override
    public KakaoEbookPreOrderResponse kakaoEbookPreOrder(
            String accessToken,
            KakaoPreOrderRequest kakaoPreOrderRequest
    ) {

        String readerId = memberServiceClient.getStudentInfo(accessToken).getStudentId();

        String itemCode = kakaoPreOrderRequest.getItemCode();

        String orderId = kakaoPreOrderRequest.getPartnerOrderId();

//      =========================================================

        OrderType orderType = kakaoPreOrderRequest.getOrderType();
        Integer quantity = kakaoPreOrderRequest.getQuantity();


        String vendorReturnUrl = kakaoProperties.getBaseUrl();
        String orderReturnUri = "order/ebook/kakao/pay/success";

        if(orderType.equals(DIRECT)) {
            redisOrderCache.saveDirectOrderInfo(orderId, new DirectOrderDto(accessToken, itemCode, quantity));
        }

        String orderCategoryIdPath = kakaoPreOrderRequest.getOrderCategory().getDescription();

        KakaoOrderReadyRequest kakaoOrderReadyRequest = KakaoOrderReadyRequest.builder()
                .cid(kakaoProperties.getCid())
                //가맹점 주문번호
                .partnerOrderId(orderId)
                .partnerUserId(kakaoProperties.getPartnerUserId())
                .itemCode(kakaoPreOrderRequest.getItemCode())
                .itemName(kakaoPreOrderRequest.getItemName())
                .quantity(kakaoPreOrderRequest.getQuantity())
                .totalAmount(kakaoPreOrderRequest.getTotalAmount())
                .taxFreeAmount(kakaoPreOrderRequest.getTaxFreeAmount())
                .vatAmount(kakaoPreOrderRequest.getVatAmount())
                .approvalUrl(vendorReturnUrl + "/" + orderReturnUri + "/" + orderType.toString().toLowerCase() + "/" + orderCategoryIdPath + "/" + orderId)
                .cancelUrl(kakaoProperties.getBaseUrl() + "/order/kakao/pay/cancel")
                .failUrl(kakaoProperties.getBaseUrl() + "/order/kakao/pay/fail")
                .build();


        KakaoEbookPreOrderResponse responseBody = webClient.post()
                .uri("https://open-api.kakaopay.com/online/v1/payment/ready")
                .headers(header -> header.addAll(getHeaders()))
                .bodyValue(kakaoOrderReadyRequest)
                .retrieve()
                .bodyToMono(KakaoEbookPreOrderResponse.class)
                .doOnError(e -> log.error("KakaoPay API 호출 실패: {}", e.getMessage()))
                .onErrorResume(e -> Mono.empty())
                .block();

        if(responseBody == null) {
            throw new IllegalArgumentException("결제 에러");
        }

        String tid = responseBody.getTid();
        redisOrderCache.saveKakaoOrderInfo(orderId, new KakaoDto(accessToken, tid, readerId, System.currentTimeMillis()));

        return responseBody;
    }

    @Override
    @Transactional
    public KakaoOrderResponse createKakaoOrder(
            OrderType orderType, OrderCategory orderCategory,
            String orderId, String pgToken
    ) {

        KakaoDto kakaoOrder = redisOrderCache.getKakaoOrderInfo(orderId);

        String studentId = kakaoOrder.getStudentId();
        String accessToken = kakaoOrder.getAccessToken();

        String username = memberServiceClient.getStudentInfo(accessToken).getUsername();

        KakaoOrderResponse response = getEbookOrderResponse(orderId, pgToken);

        // 주문 내역 등록
        switch (orderType) {
            case DIRECT:

                eventPublisher.publishEvent(new KakaoDirectOrderEvent(this, studentId, username, orderCategory, response));
                eventPublisher.publishEvent(new KakaoAfterDirectOrderEvent(this, studentId, username, orderCategory, response));
                break;

            case CART :
                break;
        }

        return response;
    }

    //카카오 공통 로직
    @Override
    @Transactional
    public KakaoEbookSearchOrderResponse searchEbookKakaoOrder(String tid) {

        ResponseEntity<KakaoEbookSearchOrderResponse> kakaoEbookSearchOrderResponse =
                getKakaoEbookSearchOrderResponseEntity(tid);

        return kakaoEbookSearchOrderResponse.getBody();
    }

    @Override
    @Transactional
    public void prepareToCancelKakaoOrder(String accessToke, CancelOrderPreRequest preRequest) {

        StudentResponse readerInfo = memberServiceClient.getStudentInfo(accessToke);
        String username = readerInfo.getUsername();
        String readerId = readerInfo.getStudentId();

        List<SingleEbookCancelOrder> singleEbookCancelOrders = preRequest.getSingleEbookCancelOrders();
        String refundReason = preRequest.getRefundReason();

        String vendorTid = "";
        int cancelAmount = 0;

        List<CancelOrderInfoResponse> ebookOrderList = new ArrayList<>();

        for(SingleEbookCancelOrder singleEbookCancelOrder : singleEbookCancelOrders) {

            String orderId = singleEbookCancelOrder.getOrderId();
            String ebookId = singleEbookCancelOrder.getEbookId();

            CancelOrderInfoResponse preCancelOrderInfoResponse = memberServiceClient.findPreCancelOrderInfo(readerId, orderId, ebookId);

            ebookOrderList.add(preCancelOrderInfoResponse);

            vendorTid = preCancelOrderInfoResponse.getVendorTid();
            cancelAmount += preCancelOrderInfoResponse.getCancelAmount();
        }

        EbookCancelOrderRequest ebookCancelOrderRequest = EbookCancelOrderRequest.builder()
                .cid(kakaoProperties.getCid())
                .tid(vendorTid)
                .cancelAmount(cancelAmount)
                .cancelTaxFreeAmount(0)
                .cancelVatAmount((int) (cancelAmount * 0.1))
                .cancelAvailableAmount(cancelAmount)
                .build();

        CancelOrderResponse cancelOrderResponse = cancelEbookKakaoOrder(ebookCancelOrderRequest);

        //주문 취소
        eventPublisher.publishEvent(new CancelOrderEvent(this, readerId, username, refundReason,
                ebookOrderList, cancelOrderResponse));
    }


    public CancelOrderResponse cancelEbookKakaoOrder(EbookCancelOrderRequest cancelOrderRequest) {

        String tid = cancelOrderRequest.getTid();

        ResponseEntity<KakaoEbookSearchOrderResponse> response =
                getKakaoEbookSearchOrderResponseEntity(tid);

        KakaoEbookSearchOrderResponse responseBody = response.getBody();

        if (responseBody == null) {
            throw new RuntimeException("Non-existent payment information");
        }

        HttpHeaders headers = getHeaders();

        HttpEntity<EbookCancelOrderRequest> cancelOrderRequestHttpEntity =
                new HttpEntity<>(cancelOrderRequest, headers);

        ResponseEntity<CancelOrderResponse> cancelOrderResponse = new RestTemplate()
                .postForEntity(
                        "https://open-api.kakaopay.com/online/v1/payment/cancel",
                        cancelOrderRequestHttpEntity,
                        CancelOrderResponse.class
                );

        return cancelOrderResponse.getBody();
    }

    private KakaoOrderResponse getEbookOrderResponse(String orderId, String pgToken) {

        HttpHeaders headers = getHeaders();

        KakaoDto kakaoOrder = redisOrderCache.getKakaoOrderInfo(orderId);

        String tid = kakaoOrder.getTid();

        KakaoEbookOrderApproveRequest kakaoEbookOrderApproveRequest = KakaoEbookOrderApproveRequest.builder()
                .tid(tid)
                .cid(kakaoProperties.getCid())
                .partnerOrderId(orderId)
                .partnerUserId(kakaoProperties.getPartnerUserId())
                .pgToken(pgToken)
                .build();

        HttpEntity<KakaoEbookOrderApproveRequest> requestEntity = new HttpEntity<>(kakaoEbookOrderApproveRequest, headers);

        ResponseEntity<KakaoOrderResponse> kakaoOrderResponse = new RestTemplate().postForEntity(
                "https://open-api.kakaopay.com/online/v1/payment/approve",
                requestEntity,
                KakaoOrderResponse.class
        );

        return kakaoOrderResponse.getBody();
    }


    private ResponseEntity<KakaoEbookSearchOrderResponse> getKakaoEbookSearchOrderResponseEntity(String tid) {

        HttpHeaders headers = getHeaders();
        KakaoEbookOrderSearchRequest orderSearchRequest = KakaoEbookOrderSearchRequest
                .builder()
                .cid(kakaoProperties.getCid())
                .tid(tid)
                .build();

        HttpEntity<KakaoEbookOrderSearchRequest> orderSearchRequestHttpEntity =
                new HttpEntity<>(orderSearchRequest, headers);

        return new RestTemplate().postForEntity(
                "https://open-api.kakaopay.com/online/v1/payment/order",
                orderSearchRequestHttpEntity,
                KakaoEbookSearchOrderResponse.class
        );

    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTHORIZATION, "DEV_SECRET_KEY " + kakaoProperties.getSecretKey());
        headers.setContentType(APPLICATION_JSON);
        return headers;
    }
}
