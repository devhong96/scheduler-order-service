package com.scheduler.orderservice.order.payment.kakao.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.scheduler.orderservice.order.common.domain.OrderCategory;
import com.scheduler.orderservice.order.common.domain.OrderType;
import jakarta.persistence.Enumerated;
import lombok.*;

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
    @ToString
    @AllArgsConstructor
    @JsonNaming(SnakeCaseStrategy.class)
    public static class KakaoPreOrderResponse {

        private String tid;
        private String nextRedirectAppUrl;
        private String nextRedirectMobileUrl;
        private String nextRedirectPcUrl;
        private String androidAppScheme;
        private String iosAppScheme;
        private String createdAt;
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
    @Setter
    public static class KakaoApproveOrderResponse {

        private String aid;                 // 요청 고유 번호
        private String tid;                 // 결제 고유 번호
        private String cid;                 // 가맹점 코드
        private String partner_order_id;    // 가맹점 주문번호
        private String partner_user_id;     // 가맹점 회원 id
        private Amount amount;
        private CardInfo cardInfo;
        private String payment_method_type; // 결제 수단, CARD 또는 MONEY 중 하나
        private String item_name;           // 상품 이름
        private String item_code;           // 상품 코드
        private Integer quantity;           // 상품 수량
        private String created_at;          // 결제 준비 요청 시각
        private String approved_at;         // 결제 승인 시각
        private String payload;             // 결제 승인 요청에 대해 저장한 값, 요청 시 전달된 내용
    }

    @Getter
    @AllArgsConstructor
    @JsonNaming(SnakeCaseStrategy.class)
    public static class Amount {

        private Integer total;
        private Integer taxFree;
        private Integer vat;
        private Integer point;
        private Integer discount;
        private Integer greenDeposit;

    }

    @Getter
    @Setter
    @JsonNaming(SnakeCaseStrategy.class)
    public static class CardInfo {

        private String kakaopayPurchaseCorp;          // 카카오페이 매입사명
        private String kakaopayPurchaseCorpCode;      // 카카오페이 매입사 코드
        private String kakaopayIssuerCorp;            // 카카오페이 발급사명
        private String kakaopayIssuerCorpCode;        // 카카오페이 발급사 코드
        private String bin;                           // 카드 BIN
        private String cardType;                      // 카드 타입
        private String installMonth;                  // 할부 개월 수
        private String approvedId;                    // 카드사 승인번호
        private String cardMid;                       // 카드사 가맹점 번호
        private String interestFreeInstall;           // 무이자할부 여부(Y/N)
        private String installmentType;               // 할부 유형 (24.02.01부터 제공)
        private String cardItemCode;                  // 카드 상품 코드

    }

    @Getter
    @Builder
    public static class KakaoOrderSearchRequest {
        private String cid; // 가맹점 코드
        private String tid; // 결제 고유번호, 20자
    }
}
