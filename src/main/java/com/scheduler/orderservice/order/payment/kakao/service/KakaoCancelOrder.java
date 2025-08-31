package com.scheduler.orderservice.order.payment.kakao.service;

import com.scheduler.orderservice.order.common.domain.Vendor;
import com.scheduler.orderservice.order.common.event.CancelOrderGateway;
import com.scheduler.orderservice.order.common.event.CancelOrderPayload;
import org.springframework.stereotype.Component;

import static com.scheduler.orderservice.order.common.domain.Vendor.KAKAO;

@Component
public class KakaoCancelOrder implements CancelOrderGateway {

    @Override
    public Vendor getVendor() {
        return KAKAO;
    }

    @Override
    public void refund(CancelOrderPayload payload) {

    }
}
