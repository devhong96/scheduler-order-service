package com.scheduler.orderservice.order.payment.common;

import com.scheduler.orderservice.order.common.domain.OrderType;
import com.scheduler.orderservice.order.common.domain.Vendor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CreateOrderProcesserFactory {

    // Spring이 @Component로 등록된 모든 OrderProcessor 구현체를 주입
    private final List<CreateOrderProcessor> processors;

    public CreateOrderProcessor findProcessor(Vendor vendor, OrderType orderType) {
        return processors.stream()
                // supports() 메서드를 이용해 현재 조건에 맞는 Processor를 찾음
                .filter(p -> p.supports(vendor, orderType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원하는 주문 처리기를 찾을 수 없습니다."));
    }
}
