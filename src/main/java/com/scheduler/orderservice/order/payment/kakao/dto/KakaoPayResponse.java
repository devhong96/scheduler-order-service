package com.scheduler.orderservice.order.payment.kakao.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class KakaoPayResponse {

    @Getter
    @ToString
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
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
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
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
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
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

}
