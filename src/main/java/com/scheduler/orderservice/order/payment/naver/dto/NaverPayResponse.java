package com.scheduler.orderservice.order.payment.naver.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

public class NaverPayResponse {

    @Getter
    @Setter
    public static class NaverPreOrderResponse {
        private String resultCode;
        private String paymentId;
        private String reservedId;
    }

    /*
        참고
        https://developers.pay.naver.com/docs/v2/api#payments-payments_apply
     */
    @Getter
    @Setter
    public static class NaverOrderResponse {
        private String code;
        private String message;
        private Body body;

        @Getter
        @Setter
        public static class Body {
            private String paymentId;
            private Detail detail;
        }

        @Getter
        @Setter
        public static class Detail {
            private String productName;
            private String merchantId;
            private String merchantName;
            private String cardNo;
            private String admissionYmdt;
            private String payHistId;
            private Integer totalPayAmount;
            private Integer applyPayAmount;
            private Integer primaryPayAmount;
            private Integer npointPayAmount;
            private Integer giftCardAmount;
            private Integer discountPayAmount;
            private Integer taxScopeAmount;
            private Integer taxExScopeAmount;
            private Integer environmentDepositAmount;
            private String primaryPayMeans;
            private String merchantPayKey;
            private String merchantUserKey;
            private String cardCorpCode;
            private String paymentId;
            private String admissionTypeCode;
            private Integer settleExpectAmount;
            private Integer payCommissionAmount;
            private String admissionState;
            private String tradeConfirmYmdt;
            private String cardAuthNo;
            private Integer cardInstCount;
            private Boolean usedCardPoint;
            private String bankCorpCode;
            private String bankAccountNo;
            private Boolean settleExpected;
            private Boolean extraDeduction;
            private String useCfmYmdt;
            private String userIdentifier;
        }
    }

    @Getter
    @Setter
    public static class SearchNaverOrderHistoryResponse {
        private String code;
        private String message;
        private Body body;

        @Getter
        @Setter
        public static class Body {
            private int responseCount;
            private int totalCount;
            private int totalPageCount;
            private int currentPageNumber;
            private List<PaymentDetail> list;

        }

        @Getter
        @Setter
        public static class PaymentDetail {
            private String cardAuthNo;
            private String bankAccountNo;
            private String bankCorpCode;
            private String paymentId;
            private String cardCorpCode;
            private int cardInstCount;
            private boolean usedCardPoint;
            private SettleInfo settleInfo;
            private String merchantName;
            private String productName;
            private String payHistId;
            private String merchantId;
            private String admissionYmdt;
            private String tradeConfirmYmdt;
            private int totalPayAmount;
            private int applyPayAmount;
            private String merchantPayKey;
            private String merchantUserKey;
            private String admissionTypeCode;
            private String primaryPayMeans;
            private String admissionState;
            private int primaryPayAmount;
            private int npointPayAmount;
            private int giftCardPayAmount;
            private int discountPayAmount;
            private int taxScopeAmount;
            private int taxExScopeAmount;
            private int environmentDepositAmount;
            private String cardNo;
            private boolean extraDeduction;
            private String useCfmYmdt;
            private String merchantExtraParameter;

        }

        @Getter
        @Setter
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class SettleInfo {
            private int primaryCommissionAmount;
            private int npointCommissionAmount;
            private int giftCardCommissionAmount;
            private int discountCommissionAmount;
            private int primarySettleAmount;
            private int npointSettleAmount;
            private int giftCardSettleAmount;
            private int discountSettleAmount;
            private int totalSettleAmount;
            private int totalCommissionAmount;
            private boolean settleCreated;

        }
    }

    @Getter
    @Setter
    public static class CancelNaverOrderDto {

        private String paymentId;
        private Integer cancelAmount;
        private String cancelReason;
        private String cancelRequester;
        //과세 대상 금액.
        private Integer taxScopeAmount;
        //면세 대상 금액.
        private Integer taxExScopeAmount;
    }

    @Getter
    @Setter
    public static class NaverCancelOrderResponse {
        private String code;
        private String message;
        private Body body;

        @Getter
        @Setter
        public static class Body {
            private String paymentId;
            private String payHistId;
            private String primaryPayMeans;

            private Long primaryPayCancelAmount;
            private Long primaryPayRestAmount;
            private Long npointCancelAmount;
            private Long npointRestAmount;
            private Long giftCardCancelAmount;
            private Long giftCardRestAmount;
            private Long discountCancelAmount;
            private Long discountRestAmount;

            @JsonFormat(pattern = "yyyyMMddHHmmss")
            private LocalDateTime cancelYmdt;

            private Long totalRestAmount;
            private Long applyRestAmount;
            private Long taxScopeAmount;
            private Long taxExScopeAmount;
            private Long taxScopeRestAmount;
            private Long taxExScopeRestAmount;
            private Long environmentDepositAmount;
            private Long environmentDepositRestAmount;
        }
    }
}
