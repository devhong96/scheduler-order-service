package com.scheduler.orderservice.order.payment.nicepay.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class NicePayResponse {

    @Getter
    @Setter
    @Builder
    public static class NicePayPreOrderResponse {

        private String clientId;

        private String method;

        private String orderId;

        private int amount;

        private String goodsName;

        private String returnUrl;
    }

    @Getter
    @Setter
    public static class NicePayOrderResponse {

        private String resultCode;
        private String resultMsg;
        private String tid;
        private String cancelledTid;
        private String orderId;
        private String ediDate;
        private String signature;
        private String status;
        private String paidAt;
        private String failedAt;
        private String cancelledAt;
        private String payMethod;
        private int amount;
        private int balanceAmt;
        private String goodsName;
        private String mallReserved;
        private boolean useEscrow;
        private String currency;
        private String channel;
        private String approveNo;
        private String buyerName;
        private String buyerTel;
        private String buyerEmail;
        private String receiptUrl;
        private String mallUserId;
        private boolean issuedCashReceipt;
        private Object coupon;
        private Card card;
        private String vbank;
        private String cancels;
        private String cashReceipts;

        @Getter
        @Setter
        public static class Card {
            private String cardCode;
            private String cardName;
            private String cardNum;
            private int cardQuota;
            private boolean isInterestFree;
            private String cardType;
            private boolean canPartCancel;
            private String acquCardCode;
            private String acquCardName;
        }
    }


    @Getter
    @Setter
    @ToString
    public static class NicePayCheckAmountResponse {

        private String resultCode;
        private String resultMsg;
        private String signature;
        private Boolean isValid;
        private String tid;

    }

    @Getter
    @Setter
    @ToString
    public static class NicePayCancelOrderResponse {

        private String resultCode;
        private String resultMsg;
        private String tid;
        private String cancelledTid;
        private String orderId;
        private String ediDate;
        private String signature;
        private String status;
        private String paidAt;
        private String failedAt;
        private String cancelledAt;
        private String payMethod;
        private Integer amount;
        private Integer balanceAmt;
        private String goodsName;
        private String mallReserved;
        private Boolean useEscrow;
        private String currency;
    }
}
