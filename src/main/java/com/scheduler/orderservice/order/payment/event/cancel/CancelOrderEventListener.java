package com.scheduler.orderservice.order.payment.event.cancel;

import com.scheduler.orderservice.order.client.MemberServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

import static com.scheduler.orderservice.order.client.dto.OrderDto.CancelOrderInfoResponse;
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

        String readerId = event.getStudentId();
        String cancelReason = event.getCancelReason();
        List<CancelOrderInfoResponse> ebookOrderList = event.getEbookOrderList();

        //TODO 로직 정리
        //읽은 도서 리스트 가져오기
        List<String> readEbookIds = memberServiceClient
                .findOwnedEbookIdsByReaderId(readerId, true);

//        List<EbookOrderInfo> updatedOrders = ebookOrderList.stream()
//                .peek(ebookOrder -> {
//                    if (readEbookIds.contains(ebookOrder.getEbookId())) {
//                        throw new RuntimeException("이미 읽은 도서: " + ebookOrder.getEbookName());
//                    }
//                    ebookOrder.updateIsCancelled(true, cancelReason);
//                }).toList(); // 업데이트된 ebookOrder 리스트
//
//        updatedOrders.forEach(ebookOrder -> {
//
//            ebook.minusSalesEbook(); // 판매량 감소
//        });
//
//        // 변경된 ebookOrder 저장
//        ebookOrderJpaRepository.saveAll(updatedOrders);
//
//        // 구매 도서 삭제
//        memberServiceClient.deleteOwnedEbookList(new DeleteOwnedEbookResponse(readerId, readEbookIds));

    }
}
