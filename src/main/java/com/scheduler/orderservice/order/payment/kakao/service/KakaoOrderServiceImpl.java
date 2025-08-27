package com.scheduler.orderservice.order.payment.kakao.service;

import com.scheduler.orderservice.infra.exception.custom.PaymentException;
import com.scheduler.orderservice.order.client.MemberServiceClient;
import com.scheduler.orderservice.order.common.component.RedisOrderCache;
import com.scheduler.orderservice.order.common.domain.OrderCategory;
import com.scheduler.orderservice.order.common.domain.OrderType;
import com.scheduler.orderservice.order.common.dto.CancelOrderRequest;
import com.scheduler.orderservice.order.common.dto.DirectOrderDto;
import com.scheduler.orderservice.order.common.dto.KakaoDto;
import com.scheduler.orderservice.order.payment.event.direct.vendor.KakaoAfterDirectOrderEvent;
import com.scheduler.orderservice.order.payment.event.direct.vendor.KakaoDirectOrderEvent;
import com.scheduler.orderservice.order.payment.kakao.service.component.ApproveKakaoOrder;
import com.scheduler.orderservice.order.payment.kakao.service.component.GetKakaoOrder;
import com.scheduler.orderservice.order.payment.kakao.service.component.KakaoPreOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import static com.scheduler.orderservice.order.common.domain.OrderType.DIRECT;
import static com.scheduler.orderservice.order.payment.kakao.dto.KakaoPayRequest.KakaoPreOrderRequest;
import static com.scheduler.orderservice.order.payment.kakao.dto.KakaoPayResponse.KakaoApproveOrderResponse;
import static com.scheduler.orderservice.order.payment.kakao.dto.KakaoPayResponse.KakaoPreOrderResponse;
import static com.scheduler.orderservice.order.payment.kakao.dto.KakaoSearchOrderDto.KakaoSearchOrderResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoOrderServiceImpl implements KakaoOrderService {

    private final MemberServiceClient memberServiceClient;
    private final GetKakaoOrder getKakaoOrder;
    private final ApproveKakaoOrder approveKakaoOrder;
    private final KakaoPreOrder kakaoPreOrder;

    private final RedisOrderCache redisOrderCache;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public KakaoPreOrderResponse kakaoPreOrder(
            String accessToken, KakaoPreOrderRequest kakaoPreOrderRequest
    ) {

        String studentId = memberServiceClient.getStudentInfo(accessToken).getStudentId();

        String itemCode = kakaoPreOrderRequest.getItemCode();

        Integer quantity = kakaoPreOrderRequest.getQuantity();

        String orderId = kakaoPreOrderRequest.getPartnerOrderId();

        OrderType orderType = kakaoPreOrderRequest.getOrderType();

        if(orderType.equals(DIRECT)) {
            redisOrderCache.saveDirectOrderInfo(orderId, new DirectOrderDto(accessToken, itemCode, quantity));
        }

        KakaoPreOrderResponse response = kakaoPreOrder.kakaoPreOrderResponse(orderId, kakaoPreOrderRequest)
                .blockOptional().orElseThrow(PaymentException::new);

        String tid = response.getTid();
        redisOrderCache.saveKakaoOrderInfo(orderId, new KakaoDto(accessToken, tid, studentId, System.currentTimeMillis()));

        return response;
    }

    @Override
    public KakaoApproveOrderResponse createKakaoOrder(
            OrderType orderType, OrderCategory orderCategory,
            String orderId, String pgToken
    ) {

        KakaoDto kakaoOrder = redisOrderCache.getKakaoOrderInfo(orderId);

        String studentId = kakaoOrder.getStudentId();
        String accessToken = kakaoOrder.getAccessToken();

        String username = memberServiceClient.getStudentInfo(accessToken).getUsername();

        KakaoApproveOrderResponse response = approveKakaoOrder.kakaoApproveOrderResponse(orderId, pgToken)
                .blockOptional().orElseThrow(PaymentException::new);

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
    public KakaoSearchOrderResponse searchKakaoOrder(String tid) {

        return getKakaoOrder.getKakaoOrderResponse(tid)
                .blockOptional().orElseThrow(PaymentException::new);
    }

    @Override
    public void prepareToCancelKakaoOrder(String accessToken, CancelOrderRequest preRequest) {

//        StudentResponse studentInfo = memberServiceClient.getStudentInfo(accessToken);
//        String username = studentInfo.getUsername();
//        String memberId = studentInfo.getStudentId();
//
//        List<SingleCancelOrder> singleCancelOrders = preRequest.getSingleCancelOrders();
//        String refundReason = preRequest.getRefundReason();
//
//        String vendorTid = "";
//        int cancelAmount = 0;
//
//        List<CancelOrderInfoResponse> orderList = new ArrayList<>();
//
//        for(SingleCancelOrder singleCancelOrder : singleCancelOrders) {
//
//            String orderId = singleCancelOrder.getOrderId();
//            String productId = singleCancelOrder.getProductId();
//
//            orderList.add(preCancelOrderInfoResponse);
//
//            vendorTid = preCancelOrderInfoResponse.getVendorTid();
//            cancelAmount += preCancelOrderInfoResponse.getCancelAmount();
//        }
//
//        KakaoCancelOrderDto.CancelOrderRequest cancelOrderRequest = KakaoCancelOrderDto.CancelOrderRequest.builder()
//                .cid(kakaoProperties.getKakaoClient().getCid())
//                .tid(vendorTid)
//                .cancelAmount(cancelAmount)
//                .cancelTaxFreeAmount(0)
//                .cancelVatAmount((int) (cancelAmount * 0.1))
//                .cancelAvailableAmount(cancelAmount)
//                .build();
//
//        CancelOrderResponse cancelOrderResponse = cancelKakaoOrder.cancelKakaoOrder(cancelOrderRequest)
//                .blockOptional().orElseThrow(PaymentException::new);
//
//        //주문 취소
//        eventPublisher.publishEvent(new CancelOrderEvent(this, memberId, username, refundReason,
//                orderList, cancelOrderResponse));
    }

}
