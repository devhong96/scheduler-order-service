package com.scheduler.orderservice.order.payment.event.cart;

import com.scheduler.orderservice.order.common.domain.OrderType;
import com.scheduler.orderservice.order.common.domain.Vendor;
import com.scheduler.orderservice.order.payment.common.CreateOrderProcessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.scheduler.orderservice.order.common.domain.OrderType.CART;
import static com.scheduler.orderservice.order.common.domain.Vendor.NICEPAY;

@Service
public class NICEPayCartCreateOrderProcessor implements CreateOrderProcessor {

    @Override
    public Boolean supports(Vendor vendor, OrderType orderType) {
        return vendor == NICEPAY && orderType == CART;
    }

    @Override
    @Transactional
    public void process() {

    }
}
