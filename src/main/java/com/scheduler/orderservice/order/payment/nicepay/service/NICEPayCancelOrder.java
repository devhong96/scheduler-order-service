package com.scheduler.orderservice.order.payment.nicepay.service;

import com.scheduler.orderservice.infra.exception.custom.PaymentException;
import com.scheduler.orderservice.order.common.domain.Vendor;
import com.scheduler.orderservice.order.common.event.CancelOrderGateway;
import com.scheduler.orderservice.order.common.event.CancelOrderPayload;
import com.scheduler.orderservice.order.payment.nicepay.service.component.CancelNicePayOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.scheduler.orderservice.order.common.domain.Vendor.NICEPAY;
import static com.scheduler.orderservice.order.payment.nicepay.dto.NicePayResponse.NicePayCancelOrderResponse;

@Component
@RequiredArgsConstructor
public class NICEPayCancelOrder implements CancelOrderGateway {

    private final CancelNicePayOrder cancelNicePayOrder;

    @Override
    public Vendor getVendor() {
        return NICEPAY;
    }

    @Override
    public void refund(CancelOrderPayload payload) {

        NicePayCancelOrderResponse orderResponse = cancelNicePayOrder.cancelNicepayOrder(payload)
                .blockOptional().orElseThrow(PaymentException::new);

    }
}
