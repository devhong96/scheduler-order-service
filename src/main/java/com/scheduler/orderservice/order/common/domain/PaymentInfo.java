package com.scheduler.orderservice.order.common.domain;

import com.scheduler.orderservice.order.payment.common.PaymentHistoryDto;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Embeddable
@NoArgsConstructor(access = PROTECTED)
public class PaymentInfo {

    private String tid;             // PG사 고유 거래 ID (TID)
    private String orderId;         // 내부 주문 ID
    private Integer totalAmount;    // 총 결제 금액
    private String paymentMethod;   // 결제 수단 (카드, 계좌 등)
    private String paymentStatus;   // 결제 상태 (승인, 취소 등)
    private String approvalNumber;  // 카드/결제 승인 번호
    private LocalDateTime paymentDateTime; // 결제/승인 시간
    private String cardNumberMasked; // 마스킹된 카드 번호
    private String cardIssuerCode;  // 카드 발급사 코드/이름
    private String merchantId;      // 상점 ID
    private String studentId;        // 사용자 식별자 (내부 유저 ID)
    private Integer taxAmount;      // 세금 금액 (VAT)
    private Integer discountAmount; // 할인 금액

    public static PaymentInfo from(PaymentHistoryDto paymentHistoryDto) {
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.tid = paymentHistoryDto.getTid();
        paymentInfo.orderId = paymentHistoryDto.getOrderId();
        paymentInfo.totalAmount = paymentHistoryDto.getTotalAmount();
        paymentInfo.paymentMethod = paymentHistoryDto.getPaymentMethod();
        paymentInfo.paymentStatus = paymentHistoryDto.getPaymentStatus();
        paymentInfo.approvalNumber = paymentHistoryDto.getApprovalNumber();
        paymentInfo.paymentDateTime = paymentHistoryDto.getPaymentDateTime();
        paymentInfo.cardNumberMasked = paymentHistoryDto.getCardNumberMasked();
        paymentInfo.cardIssuerCode = paymentHistoryDto.getCardIssuerCode();
        paymentInfo.merchantId = paymentHistoryDto.getMerchantId();
        paymentInfo.studentId = paymentHistoryDto.getStudentId();
        paymentInfo.taxAmount = paymentHistoryDto.getTaxAmount();
        paymentInfo.discountAmount = paymentHistoryDto.getDiscountAmount();

        return paymentInfo;
    }

    public PaymentHistoryDto toDto() {
        PaymentHistoryDto paymentHistoryDto = new PaymentHistoryDto();
        paymentHistoryDto.setTid(this.tid);
        paymentHistoryDto.setOrderId(this.orderId);
        paymentHistoryDto.setTotalAmount(this.totalAmount);
        paymentHistoryDto.setPaymentMethod(this.paymentMethod);
        paymentHistoryDto.setPaymentStatus(this.paymentStatus);
        paymentHistoryDto.setApprovalNumber(this.approvalNumber);
        paymentHistoryDto.setPaymentDateTime(this.paymentDateTime);
        paymentHistoryDto.setCardNumberMasked(this.cardNumberMasked);
        paymentHistoryDto.setCardIssuerCode(this.cardIssuerCode);
        paymentHistoryDto.setMerchantId(this.merchantId);
        paymentHistoryDto.setStudentId(this.studentId);
        paymentHistoryDto.setTaxAmount(this.taxAmount);
        paymentHistoryDto.setDiscountAmount(this.discountAmount);
        return paymentHistoryDto;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PaymentInfo that = (PaymentInfo) o;
        return Objects.equals(tid, that.tid)
                && Objects.equals(orderId, that.orderId)
                && Objects.equals(totalAmount, that.totalAmount)
                && Objects.equals(paymentMethod, that.paymentMethod)
                && Objects.equals(paymentStatus, that.paymentStatus)
                && Objects.equals(approvalNumber, that.approvalNumber)
                && Objects.equals(paymentDateTime, that.paymentDateTime)
                && Objects.equals(cardNumberMasked, that.cardNumberMasked)
                && Objects.equals(cardIssuerCode, that.cardIssuerCode)
                && Objects.equals(merchantId, that.merchantId)
                && Objects.equals(studentId, that.studentId)
                && Objects.equals(taxAmount, that.taxAmount)
                && Objects.equals(discountAmount, that.discountAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tid, orderId, totalAmount, paymentMethod, paymentStatus, approvalNumber, paymentDateTime, cardNumberMasked, cardIssuerCode, merchantId, studentId, taxAmount, discountAmount);
    }
}
