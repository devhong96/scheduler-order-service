package com.scheduler.orderservice.order.payment.naver.service;

import com.scheduler.orderservice.order.common.domain.OrderCategory;
import com.scheduler.orderservice.order.common.domain.OrderType;
import com.scheduler.orderservice.order.payment.naver.dto.NaverPayRequest;

import static com.scheduler.orderservice.order.payment.naver.dto.NaverPayResponse.*;

public interface NaverOrderService {

    NaverOrderResponse createNaverOrder(
            OrderType orderType, OrderCategory orderCategory, String orderId, String resultCode, String paymentId);

    NaverCancelOrderResponse cancelNaverOrder(CancelNaverOrderDto cancelNaverOrderDto);

    SearchNaverOrderResponse searchNaverOrder(String payment, NaverPayRequest.SearchNaverOrderHistoryDto searchHistory);
}
