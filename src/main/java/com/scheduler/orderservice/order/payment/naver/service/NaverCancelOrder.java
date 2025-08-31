package com.scheduler.orderservice.order.payment.naver.service;

import com.scheduler.orderservice.infra.exception.custom.PaymentException;
import com.scheduler.orderservice.order.common.domain.Vendor;
import com.scheduler.orderservice.order.common.event.CancelOrderGateway;
import com.scheduler.orderservice.order.common.event.CancelOrderPayload;
import com.scheduler.orderservice.order.payment.naver.service.component.CancelNaverOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.scheduler.orderservice.order.common.domain.Vendor.NAVER;
import static com.scheduler.orderservice.order.payment.naver.dto.NaverPayResponse.NaverCancelOrderResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class NaverCancelOrder implements CancelOrderGateway {

    private final CancelNaverOrder cancelNaverOrder;

    @Override
    public Vendor getVendor() {
        return NAVER;
    }

    @Override
    public void refund(CancelOrderPayload payload) {

        NaverCancelOrderResponse naverCancelOrderResponse = cancelNaverOrder.cancelNaverOrderResponse(payload)
                .blockOptional().orElseThrow(PaymentException::new);

    }
}
