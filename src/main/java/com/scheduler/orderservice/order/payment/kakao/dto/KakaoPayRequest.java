package com.scheduler.orderservice.order.payment.kakao.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.scheduler.orderservice.order.common.domain.OrderCategory;
import com.scheduler.orderservice.order.common.domain.OrderType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import static jakarta.persistence.EnumType.STRING;

public class KakaoPayRequest {

    @Getter
    @Setter
    public static class KakaoPreOrderRequest {

        @JsonIgnore
        private String cid;

        private String partnerOrderId;

        @JsonIgnore
        private String partnerUserId;

        private String itemName;
        private String itemCode;

        private String productCoverUrl;

        private Integer quantity;

        private Integer totalAmount;
        private Integer vatAmount;
        private Integer taxFreeAmount;

        private Integer donationAmount;

        @Enumerated(STRING)
        private OrderType orderType;

        @Enumerated(STRING)
        private OrderCategory orderCategory;

        @Builder
        public KakaoPreOrderRequest(String partnerOrderId,
                                    String itemName, String itemCode, String productCoverUrl, Integer quantity,
                                    Integer totalAmount, Integer vatAmount, Integer taxFreeAmount,
                                    Integer donationAmount, OrderType orderType,
                                    OrderCategory orderCategory) {
            this.partnerOrderId = partnerOrderId;
            this.itemName = itemName;
            this.itemCode = itemCode;
            this.productCoverUrl = productCoverUrl;
            this.quantity = quantity;
            this.totalAmount = totalAmount;
            this.vatAmount = vatAmount;
            this.taxFreeAmount = taxFreeAmount;
            this.donationAmount = donationAmount;
            this.orderType = orderType;
            this.orderCategory = orderCategory;
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @JsonNaming(SnakeCaseStrategy.class)
    public static class KakaoOrderReadyRequest {

        private String cid;
        private String partnerOrderId;
        private String partnerUserId;

        private String itemName;
        private String itemCode;

        private Integer quantity;
        private Integer totalAmount;
        private Integer taxFreeAmount;
        private Integer vatAmount;

        private String approvalUrl;
        private String cancelUrl;
        private String failUrl;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @JsonNaming(SnakeCaseStrategy.class)
    public static class KakaoOrderApproveRequest {

        private String cid;
        private String tid;
        private String partnerOrderId;
        private String partnerUserId;
        private String pgToken;
    }


    @Getter
    @Builder
    public static class KakaoOrderSearchRequest {
        private String cid; // 가맹점 코드
        private String tid; // 결제 고유번호, 20자
    }
}
