package com.scheduler.orderservice.order.common.event;

import com.scheduler.orderservice.order.common.domain.Vendor;

public interface CancelOrderGateway {

    Vendor getVendor();
    void refund(CancelOrderPayload payload);
}