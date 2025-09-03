package com.scheduler.orderservice.order.payment.common;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.scheduler.orderservice.order.payment.kakao.dto.KakaoPayResponse.KakaoApproveOrderResponse;
import static com.scheduler.orderservice.order.payment.naver.dto.NaverPayResponse.NaverOrderResponse.Detail;
import static com.scheduler.orderservice.order.payment.nicepay.dto.NicePayResponse.NicePayOrderResponse;

@Getter
@Setter
public class PaymentHistoryDto {

    private String tid;             // PG사 고유 거래 ID (TID)
    private String orderId;         // 내부 주문 ID
    private Integer totalAmount;        // 총 결제 금액
    private String paymentMethod;   // 결제 수단 (카드, 계좌 등)
    private String paymentStatus;   // 결제 상태 (승인, 취소 등)
    private String approvalNumber;  // 카드/결제 승인 번호
    private LocalDateTime paymentDateTime; // 결제/승인 시간
    private String cardNumberMasked; // 마스킹된 카드 번호
    private String cardIssuerCode;  // 카드 발급사 코드/이름
    private String merchantId;      // 상점 ID
    private String merchantUserKey;       // 사용자 식별자 (내부 유저 ID)
    private Integer taxAmount;      // 세금 금액 (VAT)
    private Integer discountAmount; // 할인 금액

    // Detail 클래스로부터 생성 (static 팩토리 메서드)
    public static PaymentHistoryDto fromDetail(Detail detail) {
        PaymentHistoryDto dto = new PaymentHistoryDto();
        dto.setTid(detail.getPayHistId() != null ? detail.getPayHistId() : detail.getMerchantPayKey());
        dto.setOrderId(detail.getMerchantPayKey() != null ? detail.getMerchantPayKey() : detail.getMerchantUserKey());
        dto.setTotalAmount(detail.getTotalPayAmount());
        dto.setPaymentMethod(detail.getPrimaryPayMeans());
        dto.setPaymentStatus(detail.getAdmissionState());
        dto.setApprovalNumber(detail.getCardAuthNo());
        dto.setPaymentDateTime(parseDateTime(detail.getAdmissionYmdt() != null ? detail.getAdmissionYmdt() : detail.getTradeConfirmYmdt()));
        dto.setCardNumberMasked(detail.getCardNo());
        dto.setCardIssuerCode(detail.getCardCorpCode());
        dto.setMerchantId(detail.getMerchantId());
        dto.setMerchantUserKey(detail.getUserIdentifier() != null ? detail.getUserIdentifier() : detail.getMerchantUserKey());
        dto.setTaxAmount(detail.getTaxScopeAmount());
        dto.setDiscountAmount(detail.getDiscountPayAmount());
        return dto;
    }

    // NicePayOrderResponse로부터 생성
    public static PaymentHistoryDto fromNicePay(NicePayOrderResponse response) {
        PaymentHistoryDto dto = new PaymentHistoryDto();
        dto.setTid(response.getTid());
        dto.setOrderId(response.getOrderId());
        dto.setTotalAmount(response.getAmount());
        dto.setPaymentMethod(response.getPayMethod());
        dto.setPaymentStatus(response.getStatus());
        dto.setApprovalNumber(response.getApproveNo() != null ? response.getApproveNo() : (response.getCard() != null ? response.getCard().getAcquCardCode() : null)); // Card 내 approveNo 가정 (클래스에 approveNo가 있음)
        dto.setPaymentDateTime(parseDateTime(response.getPaidAt()));
        dto.setCardNumberMasked(response.getCard() != null ? response.getCard().getCardNum() : null);
        dto.setCardIssuerCode(response.getCard() != null ? response.getCard().getCardCode() != null ? response.getCard().getCardCode() : response.getCard().getCardName() : null);
        dto.setMerchantId(null); // 직접 매핑 없음, 필요시 mallReserved 등에서 유추
        dto.setMerchantUserKey(response.getMallUserId());
        dto.setTaxAmount(null); // 직접 없음, amount에서 계산 가능하나 생략
        dto.setDiscountAmount(null); // coupon 등에서 유추 가능하나 생략
        return dto;
    }

    // KakaoApproveOrderResponse로부터 생성
    public static PaymentHistoryDto fromKakao(KakaoApproveOrderResponse response) {
        PaymentHistoryDto dto = new PaymentHistoryDto();
        dto.setTid(response.getTid());
        dto.setOrderId(response.getPartner_order_id());
        dto.setTotalAmount(response.getAmount() != null ? response.getAmount().getTotal() : null);
        dto.setPaymentMethod(response.getPayment_method_type());
        dto.setPaymentStatus(response.getApproved_at() != null ? "approved" : null); // 유추
        dto.setApprovalNumber(response.getCardInfo() != null ? response.getCardInfo().getApprovedId() : null);
        dto.setPaymentDateTime(parseDateTime(response.getApproved_at()));
        dto.setCardNumberMasked(null); // 직접 없음, bin으로 유추 가능하나 생략
        dto.setCardIssuerCode(response.getCardInfo() != null ? response.getCardInfo().getKakaopayIssuerCorpCode() != null ? response.getCardInfo().getKakaopayIssuerCorpCode() : response.getCardInfo().getKakaopayIssuerCorp() : null);
        dto.setMerchantId(response.getCid());
        dto.setMerchantUserKey(response.getPartner_user_id());
        dto.setTaxAmount(response.getAmount() != null ? response.getAmount().getVat() : null);
        dto.setDiscountAmount(response.getAmount() != null ? response.getAmount().getDiscount() : null);
        return dto;
    }

    // 날짜 파싱 헬퍼 메서드 (포맷에 따라 조정하세요, 예: "yyyy-MM-dd HH:mm:ss")
    private static LocalDateTime parseDateTime(String dateStr) {
        if (dateStr == null) return null;
        try {
            return LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")); // 실제 포맷에 맞게 변경
        } catch (Exception e) {
            // 로그 처리
            return null;
        }
    }
}
