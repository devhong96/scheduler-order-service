package com.scheduler.orderservice.order.payment.kakao.dto;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import static lombok.AccessLevel.PROTECTED;

public class KakaoCancelOrderDto {

    @Getter
    @Setter
    public static class CancelOrderPreRequest {

        private String refundReason;

        private List<SingleEbookCancelOrder> singleEbookCancelOrders = new ArrayList<>();

        @Getter
        @Setter
        public static class SingleEbookCancelOrder {
            private String orderId;
            private String ebookId;
        }
    }


    @Getter
    @Setter
    @Builder
    @AllArgsConstructor(access = PROTECTED)
    @JsonNaming(SnakeCaseStrategy.class)
    public static class EbookCancelOrderRequest {

        private String cid;                     // 가맹점 코드
        private String tid;                     // 결제 고유 번호
        private Integer cancelAmount;           // 취소 금액
        private Integer cancelTaxFreeAmount;    // 취소 비과세 금액
        private Integer cancelVatAmount;        // 취소 VAT 금액
        private Integer cancelAvailableAmount;  // 취소 가능 금액

    }

    @Getter
    @Setter
    @ToString
    @JsonNaming(SnakeCaseStrategy.class)
    public static class CancelOrderResponse {

        private String aid; // 요청 고유 번호
        private String tid; // 결제 고유 번호
        private String cid; // 가맹점 코드
        private String status; // 결제 상태
        private String partnerOrderId; // 가맹점 주문 번호
        private String partnerUserId; // 가맹점 회원 ID
        private String paymentMethodType; // 결제 수단
        private Amount amount; // 결제 금액 정보
        private ApprovedCancelAmount approvedCancelAmount; // 이번 요청으로 취소된 금액
        private CanceledAmount canceledAmount; // 누계 취소 금액
        private CancelAvailableAmount cancelAvailableAmount; // 남은 취소 금액
        private String itemName; // 상품 이름
        private String itemCode; // 상품 코드
        private Integer quantity; // 상품 수량
        private String createdAt; // 결제 준비 요청 시각
        private String approvedAt; // 결제 승인 시각
        private String canceledAt; // 결제 취소 시각
        private String payload; // 취소 요청 시 전달한 값
    }


    @Getter
    @Setter
    @ToString
    @JsonNaming(SnakeCaseStrategy.class)
    public static class Amount {

        private Integer total;
        private Integer taxFree;
        private Integer vat;
        private Integer point;
        private Integer discount;
        private Integer greenDeposit;

    }

    /**
     * 이번 요청으로 취소된 금액
     */
    @Getter
    @Setter
    @ToString
    @JsonNaming(SnakeCaseStrategy.class)
    public static class ApprovedCancelAmount {

        private Integer total; // 이번 요청으로 취소된 전체 금액
        private Integer taxFree; // 이번 요청으로 취소된 비과세 금액
        private Integer vat; // 이번 요청으로 취소된 부가세 금액
        private Integer point; // 이번 요청으로 취소된 포인트 금액
        private Integer discount; // 이번 요청으로 취소된 할인 금액
        private Integer greenDeposit; // 컵 보증금
    }

    /**
     * 누계 취소 금액
     */
    @Getter
    @Setter
    @ToString
    @JsonNaming(SnakeCaseStrategy.class)
    public static class CanceledAmount {

        private Integer total; // 취소된 전체 누적 금액
        private Integer taxFree; // 취소된 비과세 누적 금액
        private Integer vat; // 취소된 부가세 누적 금액
        private Integer point; // 취소된 포인트 누적 금액
        private Integer discount; // 취소된 할인 누적 금액
        private Integer greenDeposit; // 컵 보증금
    }

    /**
     * 취소 요청 시 전달한 값
     */
    @Getter
    @Setter
    @ToString
    @JsonNaming(SnakeCaseStrategy.class)
    public static class CancelAvailableAmount {

        private Integer total; // 전체 취소 가능 금액
        private Integer taxFree; // 취소 가능 비과세 금액
        private Integer vat; // 취소 가능 부가세 금액
        private Integer point; // 취소 가능 포인트 금액
        private Integer discount; // 취소 가능 할인 금액
        private Integer greenDeposit; // 컵 보증금
    }
}
