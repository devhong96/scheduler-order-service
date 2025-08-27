package com.scheduler.orderservice.order.common.event;

import com.scheduler.orderservice.order.common.domain.Vendor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CreatePaymentGatewayFactory {

    private final Map<Vendor, CreateOrderGateway> gatewayMap;

    // 생성자 주입을 통해 Spring이 Component로 등록된 모든 CreateOrderGateway 구현체를 Map으로 주입해줌
    public CreatePaymentGatewayFactory(List<CreateOrderGateway> gateways) {
        this.gatewayMap = gateways.stream()
                .collect(Collectors.toMap(CreateOrderGateway::getVendor, gateway -> gateway));
    }

    // 주문의 pgProvider 값에 맞는 구현체를 찾아서 반환
    public CreateOrderGateway getVendor(Vendor vendor) {
        CreateOrderGateway gateway = gatewayMap.get(vendor);
        if (gateway == null) {
            throw new IllegalArgumentException("지원하지 않는 PG사입니다: " + vendor);
        }
        return gateway;
    }
}
