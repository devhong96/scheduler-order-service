package com.scheduler.orderservice.order.payment.nicepay.dto;

import lombok.Getter;
import lombok.Setter;

public class NicePayRequest {

    @Getter
    @Setter
    public static class NicePayPreOrderRequest {

        private String authResultCode;

        private String authResultMsg;

        private String tid;

        private String clientId;

        private String orderId;

        private Integer amount;

        private String mallReserved;

        private String authToken;

        private String signature;
    }

    @Getter
    @Setter
    public static class NicePayCancelOrderRequest {

        private String tid;

        private String reason;

        private String cancelAmt;
    }
}
