package com.scheduler.orderservice.order.payment.processor.direct;

import com.scheduler.orderservice.order.common.domain.*;
import com.scheduler.orderservice.order.common.dto.DirectOrderDto;
import com.scheduler.orderservice.order.common.repository.OrderItemJpaRepository;
import com.scheduler.orderservice.order.common.repository.OrdersJpaRepository;
import com.scheduler.orderservice.order.payment.common.CreateOrderProcessor;
import com.scheduler.orderservice.order.payment.common.PaymentHistoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.scheduler.orderservice.order.client.dto.MemberFeignDto.StudentResponse;
import static com.scheduler.orderservice.order.common.domain.OrderType.DIRECT;
import static com.scheduler.orderservice.order.common.domain.Vendor.KAKAO;

@Service
@RequiredArgsConstructor
public class KakaoCreateDirectOrder implements CreateOrderProcessor {

    private final OrdersJpaRepository ordersJpaRepository;
    private final OrderItemJpaRepository orderItemJpaRepository;

    @Override
    public Boolean supports(Vendor vendor, OrderType orderType) {
        return vendor == KAKAO && orderType == DIRECT;
    }

    @Override
    @Transactional
    public void process(String orderId, OrderType orderType, OrderCategory orderCategory, StudentResponse studentResponse,
                        DirectOrderDto directOrderDto, PaymentHistoryDto paymentHistoryDto
    ) {
        OrderItems orderItems = OrderItems.create(orderId, orderType,
                orderCategory, directOrderDto.getProductId(), directOrderDto.getProductName(), directOrderDto.getQuantity());
        orderItemJpaRepository.save(orderItems);

        Orders orders = Orders.create(orderId, KAKAO, studentResponse, paymentHistoryDto);
        ordersJpaRepository.save(orders);
    }
}
