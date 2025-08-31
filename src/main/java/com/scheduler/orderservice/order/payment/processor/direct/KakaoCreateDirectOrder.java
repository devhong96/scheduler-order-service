package com.scheduler.orderservice.order.payment.processor.direct;

import com.scheduler.orderservice.order.common.domain.OrderCategory;
import com.scheduler.orderservice.order.common.domain.OrderType;
import com.scheduler.orderservice.order.common.domain.Orders;
import com.scheduler.orderservice.order.common.domain.Vendor;
import com.scheduler.orderservice.order.common.dto.DirectOrderDto;
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

    @Override
    public Boolean supports(Vendor vendor, OrderType orderType) {
        return vendor == KAKAO && orderType == DIRECT;
    }

    @Override
    @Transactional
    public void process(OrderType orderType, OrderCategory orderCategory, StudentResponse studentResponse,
                        DirectOrderDto directOrderDto, PaymentHistoryDto paymentHistoryDto
    ) {
        Orders orders = Orders.create(KAKAO, DIRECT, orderCategory, studentResponse,
                directOrderDto.getProductId(), directOrderDto.getProductName(), directOrderDto.getQuantity(),
                paymentHistoryDto);

        ordersJpaRepository.save(orders);
    }
}
