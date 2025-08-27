package com.scheduler.orderservice.order.common.event;

import com.scheduler.orderservice.order.common.domain.Vendor;
import com.scheduler.orderservice.order.common.dto.OrderCheckoutInfo;

import static com.scheduler.orderservice.order.common.dto.OrderResponseList.OrderResponse;

public interface CreateOrderGateway {

    Vendor getVendor();

    OrderResponse createOrder(OrderCheckoutInfo orderCheckoutInfo);

}
