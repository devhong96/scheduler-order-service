package com.scheduler.orderservice.order.payment.naver.service;

import com.scheduler.orderservice.order.common.domain.OrderCategory;
import com.scheduler.orderservice.order.common.domain.OrderType;
import reactor.core.publisher.Mono;

import static com.scheduler.orderservice.order.payment.naver.dto.NaverPayRequest.SearchNaverOrderHistoryDto;
import static com.scheduler.orderservice.order.payment.naver.dto.NaverPayResponse.NaverOrderResponse;
import static com.scheduler.orderservice.order.payment.naver.dto.NaverPayResponse.SearchNaverOrderResponse;

public interface NaverOrderService {

    Mono<NaverOrderResponse> createNaverOrder(
            OrderType orderType, OrderCategory orderCategory, String orderId, String resultCode, String paymentId);

    SearchNaverOrderResponse searchNaverOrder(String payment, SearchNaverOrderHistoryDto searchHistory);
}
