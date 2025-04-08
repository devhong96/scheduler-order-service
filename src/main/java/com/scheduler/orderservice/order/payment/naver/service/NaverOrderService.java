package com.scheduler.orderservice.order.payment.naver.service;

import com.scheduler.orderservice.order.common.domain.OrderCategory;
import com.scheduler.orderservice.order.common.domain.OrderType;

import static com.scheduler.orderservice.order.payment.naver.dto.NaverPayResponse.NaverOrderResponse;

public interface NaverOrderService {

    NaverOrderResponse createNaverOrder(
            String orderId, OrderType orderType, OrderCategory orderCategory, String resultCode, String paymentId);

}
