package com.scheduler.orderservice.order.common.service;

import com.scheduler.orderservice.order.common.domain.OrderCategory;
import com.scheduler.orderservice.order.common.domain.OrderType;
import com.scheduler.orderservice.order.common.domain.Vendor;

import static com.scheduler.orderservice.order.common.dto.OrderDto.PreOrderRequest;
import static com.scheduler.orderservice.order.common.dto.OrderResponseList.OrderResponse;

public interface OrderService {

    OrderResponse createOrder(
            String accessToken,
            OrderType orderType, OrderCategory orderCategory, Vendor vendor,
            PreOrderRequest preOrderRequest);
}
