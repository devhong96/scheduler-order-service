package com.scheduler.orderservice.order.payment.nicepay.service;

import com.scheduler.orderservice.order.common.domain.OrderCategory;
import com.scheduler.orderservice.order.common.domain.OrderType;

import static com.scheduler.orderservice.order.payment.nicepay.dto.NicePayRequest.NicePayCancelOrderRequest;
import static com.scheduler.orderservice.order.payment.nicepay.dto.NicePayRequest.NicePayPreOrderRequest;

public interface NicePayService {

    void createNicePayOrder(
            String orderId,
            OrderType orderType, OrderCategory orderCategory,
            NicePayPreOrderRequest nicePayPreOrderRequest);

    void cancelEbookNicepayOrder(NicePayCancelOrderRequest nicePayCancelOrderRequest);
}
