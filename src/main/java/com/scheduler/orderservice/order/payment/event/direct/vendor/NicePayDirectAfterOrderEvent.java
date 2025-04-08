package com.scheduler.orderservice.order.payment.event.direct.vendor;

import com.scheduler.orderservice.order.common.domain.OrderCategory;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import static com.scheduler.orderservice.order.payment.nicepay.dto.NicePayResponse.NicePayOrderResponse;

@Getter
public class NicePayDirectAfterOrderEvent extends ApplicationEvent {

    private final String studentId;

    private final String username;

    private final OrderCategory orderCategory;

    private final NicePayOrderResponse nicePayOrderResponse;

    public NicePayDirectAfterOrderEvent(Object source, String studentId, String username, OrderCategory orderCategory, NicePayOrderResponse nicePayOrderResponse) {
        super(source);
        this.studentId = studentId;
        this.username = username;
        this.orderCategory = orderCategory;
        this.nicePayOrderResponse = nicePayOrderResponse;
    }
}
