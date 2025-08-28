package com.scheduler.orderservice.order.payment.event.direct;

import com.scheduler.orderservice.order.common.domain.OrderType;
import com.scheduler.orderservice.order.common.domain.Vendor;
import com.scheduler.orderservice.order.payment.common.CreateOrderProcessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.scheduler.orderservice.order.common.domain.OrderType.DIRECT;
import static com.scheduler.orderservice.order.common.domain.Vendor.KAKAO;

@Service
public class KakaoDirectCreateOrderProcessor implements CreateOrderProcessor {

    @Override
    public Boolean supports(Vendor vendor, OrderType orderType) {
        return vendor == KAKAO && orderType == DIRECT;
    }

    @Override
    @Transactional
    public void process() {

    }
}
