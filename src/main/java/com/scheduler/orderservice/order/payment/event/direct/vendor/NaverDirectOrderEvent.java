package com.scheduler.orderservice.order.payment.event.direct.vendor;

import com.scheduler.orderservice.order.common.domain.OrderCategory;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import static com.scheduler.orderservice.order.payment.naver.dto.NaverPayResponse.NaverOrderResponse;

@Getter
public class NaverDirectOrderEvent extends ApplicationEvent {

    private final String studentId;

    private final String username;

    private final Integer quantity;

    private final OrderCategory orderCategory;

    private final NaverOrderResponse naverOrderResponse;

    public NaverDirectOrderEvent(Object source, String studentId, String username, Integer quantity, OrderCategory orderCategory, NaverOrderResponse naverOrderResponse) {
        super(source);
        this.studentId = studentId;
        this.username = username;
        this.quantity = quantity;
        this.orderCategory = orderCategory;
        this.naverOrderResponse = naverOrderResponse;
    }
}
