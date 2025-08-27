package com.scheduler.orderservice.order.payment.kakao.service;

import com.scheduler.orderservice.order.common.domain.OrderCategory;
import com.scheduler.orderservice.order.common.domain.OrderType;
import com.scheduler.orderservice.order.common.dto.CancelOrderRequest;

import static com.scheduler.orderservice.order.payment.kakao.dto.KakaoPayRequest.KakaoPreOrderRequest;
import static com.scheduler.orderservice.order.payment.kakao.dto.KakaoPayResponse.KakaoApproveOrderResponse;
import static com.scheduler.orderservice.order.payment.kakao.dto.KakaoPayResponse.KakaoPreOrderResponse;
import static com.scheduler.orderservice.order.payment.kakao.dto.KakaoSearchOrderDto.KakaoSearchOrderResponse;

public interface KakaoOrderService {

    KakaoPreOrderResponse kakaoPreOrder(String accessToken, KakaoPreOrderRequest kakaoPreOrderRequest);

    KakaoApproveOrderResponse createKakaoOrder(OrderType orderType, OrderCategory orderCategory, String orderId, String pgToken);

    KakaoSearchOrderResponse searchKakaoOrder(String tid);

    void prepareToCancelKakaoOrder(String accessToke, CancelOrderRequest cancelOrderRequest);
}
