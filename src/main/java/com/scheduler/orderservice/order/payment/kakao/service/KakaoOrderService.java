package com.scheduler.orderservice.order.payment.kakao.service;

import com.scheduler.orderservice.order.common.domain.OrderCategory;
import com.scheduler.orderservice.order.common.domain.OrderType;

import static com.scheduler.orderservice.order.payment.kakao.dto.KakaoCancelOrderDto.CancelOrderPreRequest;
import static com.scheduler.orderservice.order.payment.kakao.dto.KakaoPayRequest.*;
import static com.scheduler.orderservice.order.payment.kakao.dto.KakaoSearchOrderDto.KakaoEbookSearchOrderResponse;

public interface KakaoOrderService {

    KakaoEbookPreOrderResponse kakaoEbookPreOrder(String accessToken, KakaoPreOrderRequest kakaoPreOrderRequest);

    KakaoOrderResponse createKakaoOrder(OrderType orderType, OrderCategory orderCategory, String orderId, String pgToken);

    KakaoEbookSearchOrderResponse searchEbookKakaoOrder(String tid);

    void prepareToCancelKakaoOrder(String accessToke, CancelOrderPreRequest cancelOrderPreRequest);
}
