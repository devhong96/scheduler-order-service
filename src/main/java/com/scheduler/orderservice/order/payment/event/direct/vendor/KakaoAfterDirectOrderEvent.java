package com.scheduler.orderservice.order.payment.event.direct.vendor;

import com.scheduler.orderservice.order.common.domain.OrderCategory;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import static com.scheduler.orderservice.order.payment.kakao.dto.KakaoPayRequest.KakaoOrderResponse;

@Getter
public class KakaoAfterDirectOrderEvent extends ApplicationEvent {

    private final String studentId;

    private final String username;

    private final OrderCategory orderCategory;

    private final KakaoOrderResponse kakaoOrderResponse;

    public KakaoAfterDirectOrderEvent(Object source, String studentId, String username, OrderCategory orderCategory, KakaoOrderResponse kakaoOrderResponse) {
        super(source);
        this.studentId = studentId;
        this.username = username;
        this.orderCategory = orderCategory;
        this.kakaoOrderResponse = kakaoOrderResponse;
    }
}
