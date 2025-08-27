package com.scheduler.orderservice.order.common.service;

import com.scheduler.orderservice.infra.exception.custom.OrderExistException;
import com.scheduler.orderservice.order.common.domain.Orders;
import com.scheduler.orderservice.order.common.domain.Vendor;
import com.scheduler.orderservice.order.common.dto.CancelOrderRequest;
import com.scheduler.orderservice.order.common.event.CancelOrderEventPayload;
import com.scheduler.orderservice.order.common.repository.OrdersJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderTransactionService {

    private final OrdersJpaRepository ordersJpaRepository;

    @Transactional
    public CancelOrderEventPayload cancelOrderEvent(
            String orderId, String memberId, CancelOrderRequest cancelOrderRequest
    ) {

        Orders orders = ordersJpaRepository.findOrdersByMemberIdAndOrderId(memberId, orderId)
                .orElseThrow(OrderExistException::new);

        String vendorTid = orders.getVendorTid();
        Vendor vendor = orders.getVendor();
        int cancelAmount = 0;

        //TODO 엔티티 취소 로직 진행

        return null;
    }

}

