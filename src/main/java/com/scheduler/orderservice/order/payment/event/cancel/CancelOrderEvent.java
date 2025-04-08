package com.scheduler.orderservice.order.payment.event.cancel;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

import static com.scheduler.orderservice.order.client.dto.OrderDto.CancelOrderInfoResponse;
import static com.scheduler.orderservice.order.payment.kakao.dto.KakaoCancelOrderDto.CancelOrderResponse;

@Getter
public class CancelOrderEvent extends ApplicationEvent {

    private final String studentId;

    private final String username;

    private final String cancelReason;

    private final List<CancelOrderInfoResponse> ebookOrderList;

    private final CancelOrderResponse cancelOrderResponse;

    public CancelOrderEvent(Object source, String studentId, String username, String cancelReason,
                            List<CancelOrderInfoResponse> ebookOrderList, CancelOrderResponse cancelOrderResponse) {
        super(source);
        this.studentId = studentId;
        this.username = username;
        this.cancelReason = cancelReason;
        this.ebookOrderList = ebookOrderList;
        this.cancelOrderResponse = cancelOrderResponse;
    }
}
