package com.scheduler.orderservice.order.payment.kakao.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class KakaoSearchOrderDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class KakaoEbookCancelOrderInfo {

        private String cid; // 가맹점 코드
        private String tid; // 결제 고유번호, 20자
    }

    @Getter
    @Setter
    @ToString
    public static class KakaoSearchOrderResponse {

        private String tid;
        private String cid;
        private String status;
        private String partnerOrderId;
        private String partnerUserId;
        private String paymentMethodType;
        private String itemName;
        private int quantity;
        private Amount amount;
        private CanceledAmount canceledAmount;
        private CancelAvailableAmount cancelAvailableAmount;
        private LocalDateTime createdAt;
        private LocalDateTime approvedAt;
        private List<PaymentActionDetails> paymentActionDetails;
    }

    @Getter
    @Setter
    @ToString
    public static class Amount {

        private int total;
        private int taxFree;
        private int vat;
        private int point;
        private int discount;
        private int greenDeposit;
    }

    @Getter
    @Setter
    @ToString
    public static class CanceledAmount {
        private int total;
        private int taxFree;
        private int vat;
        private int point;
        private int discount;
        private int greenDeposit;
    }

    @Getter
    @Setter
    @ToString
    public static class CancelAvailableAmount {
        private int total;
        private int taxFree;
        private int vat;
        private int point;
        private int discount;
        private int greenDeposit;
    }

    @Getter
    @Setter
    @ToString
    public static class PaymentActionDetails {
        private String aid;
        private String paymentActionType;
        private String paymentMethodType;
        private int amount;
        private int pointAmount;
        private int discountAmount;
        private LocalDateTime approvedAt;
        private int greenDeposit;

    }
}
