package com.scheduler.orderservice.order.payment.kakao.service;

import com.scheduler.orderservice.infra.exception.custom.PaymentException;
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
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import static com.scheduler.orderservice.order.payment.kakao.dto.KakaoSearchOrderDto.KakaoSearchOrderResponse;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
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
    public KakaoPreOrderResponse kakaoPreOrder(
            String accessToken,
            KakaoPreOrderRequest kakaoPreOrderRequest
    ) {

        String readerId = memberServiceClient.getStudentInfo(accessToken).getStudentId();

        String itemCode = kakaoPreOrderRequest.getItemCode();

        String orderId = kakaoPreOrderRequest.getPartnerOrderId();

//      =========================================================

        OrderType orderType = kakaoPreOrderRequest.getOrderType();
        Integer quantity = kakaoPreOrderRequest.getQuantity();


        String vendorReturnUrl = kakaoProperties.getKakaoUrl().getBaseUrl();
        String orderReturnUri = "order/kakao/pay/success";

        if(orderType.equals(DIRECT)) {
            redisOrderCache.saveDirectOrderInfo(orderId, new DirectOrderDto(accessToken, itemCode, quantity));
        }

        String orderCategoryIdPath = kakaoPreOrderRequest.getOrderCategory().getDescription();

        KakaoOrderReadyRequest kakaoOrderReadyRequest = KakaoOrderReadyRequest.builder()
                .cid(kakaoProperties.getKakaoClient().getCid())
                //가맹점 주문번호
                .partnerOrderId(orderId)
                .partnerUserId(kakaoProperties.getKakaoClient().getPartnerUserId())
                .itemCode(kakaoPreOrderRequest.getItemCode())
                .itemName(kakaoPreOrderRequest.getItemName())
                .quantity(kakaoPreOrderRequest.getQuantity())
                .totalAmount(kakaoPreOrderRequest.getTotalAmount())
                .taxFreeAmount(kakaoPreOrderRequest.getTaxFreeAmount())
                .vatAmount(kakaoPreOrderRequest.getVatAmount())
                .approvalUrl(vendorReturnUrl + "/" + orderReturnUri + "/" + orderType.toString().toLowerCase() + "/" + orderCategoryIdPath + "/" + orderId)
                .cancelUrl(kakaoProperties.getKakaoUrl().getCancelUrl() + "/order/kakao/pay/cancel")
                .failUrl(kakaoProperties.getKakaoUrl().getBaseUrl() + "/order/kakao/pay/fail")
                .build();


        Mono<KakaoPreOrderResponse> kakaoEbookPreOrderResponseMono = webClient.post()
                .uri(kakaoProperties.getKakaoUrl().getReadyUrl())
                .headers(header -> header.addAll(getHeaders()))
                .bodyValue(kakaoOrderReadyRequest)
                .retrieve()
                .bodyToMono(KakaoPreOrderResponse.class);

        KakaoPreOrderResponse response = kakaoEbookPreOrderResponseMono.blockOptional()
                .orElseThrow(() -> new PaymentException("결제 준비 응답이 null 입니다"));
        String tid = response.getTid();
        redisOrderCache.saveKakaoOrderInfo(orderId, new KakaoDto(accessToken, tid, readerId, System.currentTimeMillis()));

        return response;
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

        KakaoOrderResponse response = getOrderResponse(orderId, pgToken);

        // 주문 내역 등록
        switch (orderType) {
            case DIRECT:

                eventPublisher.publishEvent(new KakaoDirectOrderEvent(this, studentId, username, 1, orderCategory, response));
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
    public KakaoSearchOrderResponse searchKakaoOrder(String tid) {

        return getKakaoSearchOrderResponseEntity(tid);
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
                .cid(kakaoProperties.getKakaoClient().getCid())
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

        getKakaoSearchOrderResponseEntity(tid);

        Mono<CancelOrderResponse> cancelOrderResponseMono = webClient.post()
                .uri(kakaoProperties.getKakaoUrl().getCancelUrl())
                .headers(h -> h.addAll(getHeaders()))
                .bodyValue(cancelOrderRequest)
                .retrieve()
                .bodyToMono(CancelOrderResponse.class);

        return cancelOrderResponseMono.blockOptional().orElseThrow(() -> new PaymentException("결제 준비 응답이 null 입니다"));
    }

    private KakaoOrderResponse getOrderResponse(String orderId, String pgToken) {

        KakaoDto kakaoOrder = redisOrderCache.getKakaoOrderInfo(orderId);

        String tid = kakaoOrder.getTid();

        KakaoOrderApproveRequest request = KakaoOrderApproveRequest.builder()
                .tid(tid)
                .cid(kakaoProperties.getKakaoClient().getCid())
                .partnerOrderId(orderId)
                .partnerUserId(kakaoProperties.getKakaoClient().getPartnerUserId())
                .pgToken(pgToken)
                .build();

        Mono<KakaoOrderResponse> kakaoOrderResponseMono = webClient.post()
                .uri(kakaoProperties.getKakaoUrl().getApproveUrl())
                .headers(header -> header.addAll(getHeaders()))
                .bodyValue(request)
                .retrieve()
                .bodyToMono(KakaoOrderResponse.class);

        return kakaoOrderResponseMono.blockOptional().orElseThrow(() -> new PaymentException("결제 준비 응답이 null 입니다"));
    }


    private KakaoSearchOrderResponse getKakaoSearchOrderResponseEntity(String tid) {

        KakaoOrderSearchRequest orderSearchRequest = KakaoOrderSearchRequest
                .builder()
                .cid(kakaoProperties.getKakaoClient().getCid())
                .tid(tid)
                .build();

        Mono<KakaoSearchOrderResponse> kakaoSearchOrderResponse = webClient.post()
                .uri(kakaoProperties.getKakaoUrl().getOrderUrl())
                .headers(header -> header.addAll(getHeaders()))
                .bodyValue(orderSearchRequest)
                .retrieve()
                .bodyToMono(KakaoSearchOrderResponse.class);

        return kakaoSearchOrderResponse.blockOptional().orElseThrow(() -> new PaymentException("결제 준비 응답이 null입니다"));


    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTHORIZATION, "DEV_SECRET_KEY " + kakaoProperties.getKakaoClient().getSecretKey());
        headers.setContentType(APPLICATION_JSON);
        return headers;
    }
}
