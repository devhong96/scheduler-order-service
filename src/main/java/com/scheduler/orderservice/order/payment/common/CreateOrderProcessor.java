package com.scheduler.orderservice.order.payment.common;

import com.scheduler.orderservice.order.common.domain.OrderType;
import com.scheduler.orderservice.order.common.domain.Vendor;

public interface CreateOrderProcessor {

    Boolean supports(Vendor vendor, OrderType orderType);

    void process();
}
