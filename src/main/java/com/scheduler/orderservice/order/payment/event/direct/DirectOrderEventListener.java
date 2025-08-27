package com.scheduler.orderservice.order.payment.event.direct;

import com.scheduler.orderservice.order.common.domain.OrderCategory;
import com.scheduler.orderservice.order.payment.event.direct.vendor.KakaoDirectOrderEvent;
import com.scheduler.orderservice.order.payment.event.direct.vendor.NaverDirectOrderEvent;
import com.scheduler.orderservice.order.payment.event.direct.vendor.NicePayDirectOrderEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import static com.scheduler.orderservice.order.payment.kakao.dto.KakaoPayResponse.KakaoApproveOrderResponse;
import static com.scheduler.orderservice.order.payment.naver.dto.NaverPayResponse.NaverOrderResponse.Detail;
import static com.scheduler.orderservice.order.payment.nicepay.dto.NicePayResponse.NicePayOrderResponse;
import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

@Slf4j
@Component
@RequiredArgsConstructor
public class DirectOrderEventListener {

    @Async
    @TransactionalEventListener(phase = AFTER_COMMIT)
    public void handleKakaoDirectOrderEventListener(KakaoDirectOrderEvent event) {

        String studentId = event.getStudentId();
        String username = event.getUsername();
        Integer quantity = event.getQuantity();

        OrderCategory orderCategory = event.getOrderCategory();
        KakaoApproveOrderResponse response = event.getKakaoApproveOrderResponse();


    }

    @Async
    @TransactionalEventListener(phase = AFTER_COMMIT)
    public void handleNaverDirectOrderEventListener(NaverDirectOrderEvent event) {

        String studentId = event.getStudentId();
        String username = event.getUsername();
        Integer quantity = event.getQuantity();

        OrderCategory orderCategory = event.getOrderCategory();
        Detail detail = event.getNaverOrderResponse().getBody().getDetail();


    }

    @Async
    @TransactionalEventListener(phase = AFTER_COMMIT)
    public void handleNicePayDirectOrderEventListener(NicePayDirectOrderEvent event) {

        String studentId = event.getStudentId();
        String username = event.getUsername();

        Integer quantity = event.getQuantity();

        OrderCategory orderCategory = event.getOrderCategory();
        NicePayOrderResponse response = event.getNicePayOrderResponse();

    }
}
