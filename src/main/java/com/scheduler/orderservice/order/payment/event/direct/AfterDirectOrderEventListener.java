package com.scheduler.orderservice.order.payment.event.direct;

import com.scheduler.orderservice.order.payment.event.direct.vendor.KakaoAfterDirectOrderEvent;
import com.scheduler.orderservice.order.payment.event.direct.vendor.NaverAfterDirectOrderEvent;
import com.scheduler.orderservice.order.payment.event.direct.vendor.NicePayDirectAfterOrderEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import static com.scheduler.orderservice.order.common.domain.OrderCategory.TUITION;
import static com.scheduler.orderservice.order.payment.kakao.dto.KakaoPayRequest.KakaoApproveOrderResponse;
import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

@Slf4j
@Component
@RequiredArgsConstructor
public class AfterDirectOrderEventListener {

    @Async
    @TransactionalEventListener(phase = AFTER_COMMIT)
    public void handleKakaoEventListener(KakaoAfterDirectOrderEvent event) {

        KakaoApproveOrderResponse response = event.getKakaoApproveOrderResponse();

        String orderId = response.getPartner_order_id();

        if (event.getOrderCategory().equals(TUITION)) {
            String studentId = event.getStudentId();

        }
    }

    @Async
    @TransactionalEventListener(phase = AFTER_COMMIT)
    public void handleNaverEventListener(NaverAfterDirectOrderEvent event) {

        String orderId = event.getNaverOrderResponse().getBody().getDetail().getMerchantPayKey();

        if(event.getOrderCategory().equals(TUITION)) {
            String studentId = event.getStudentId();

        }
    }

    @Async
    @TransactionalEventListener(phase = AFTER_COMMIT)
    public void handleNicepayEventListener(NicePayDirectAfterOrderEvent event) {


        String orderId = event.getNicePayOrderResponse().getOrderId();

        if(event.getOrderCategory().equals(TUITION)) {
            String studentId = event.getStudentId();

        }

    }
}
