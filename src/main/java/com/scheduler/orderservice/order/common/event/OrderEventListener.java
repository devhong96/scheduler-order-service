package com.scheduler.orderservice.order.common.event;

import com.scheduler.orderservice.order.common.domain.Vendor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventListener {

    private final CancelPaymentGatewayFactory cancelOrderFactory;


    @Async
    @EventListener
    public void handleCancelOrder(CancelOrderEventPayload payload) {

        Vendor vendor = payload.getVendor();

        CancelOrderGateway gateway = cancelOrderFactory.getVendor(vendor);

        gateway.refund(payload);
    }
}
