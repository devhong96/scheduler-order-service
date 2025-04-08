package com.scheduler.orderservice.order.payment.event.cancel;

import com.scheduler.orderservice.order.client.MemberServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;
import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

@Slf4j
@Component
@RequiredArgsConstructor
public class CancelOrderEventListener {

    private final MemberServiceClient memberServiceClient;

    @Async
    @Transactional(propagation = REQUIRES_NEW)
    @TransactionalEventListener(phase = AFTER_COMMIT)
    public void handleKakaoCancelOrderEventListener(CancelOrderEvent event) {

        String studentId = event.getStudentId();
        String cancelReason = event.getCancelReason();

    }
}
