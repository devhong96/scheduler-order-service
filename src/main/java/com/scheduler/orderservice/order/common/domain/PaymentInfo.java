package com.scheduler.orderservice.order.common.domain;

import com.scheduler.orderservice.order.payment.common.PaymentHistoryDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class PaymentInfo {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tid;                     // PG사 고유 거래 ID (TID)

    @Column(nullable = false)
    private Integer totalAmount;            // 총 결제 금액

    @Column(nullable = false)
    private String paymentMethod;           // 결제 수단 (카드, 계좌 등)

    @Column(nullable = false)
    private String paymentStatus;           // 결제 상태 (승인, 취소 등)

    @Column(nullable = false)
    private String approvalNumber;          // 카드/결제 승인 번호

    @Column(nullable = false)
    private LocalDateTime paymentDateTime;  // 결제/승인 시간

    @Column(nullable = false)
    private String cardNumberMasked;        // 마스킹된 카드 번호

    @Column(nullable = false)
    private String cardIssuerCode;          // 카드 발급사 코드/이름

    @Column(nullable = false)
    private String merchantId;              // 상점 ID

    @Column(nullable = false)
    private Integer taxAmount;              // 세금 금액 (VAT)

    @Column(nullable = false)
    private Integer discountAmount;         // 할인 금액

    public static PaymentInfo create(PaymentHistoryDto paymentHistoryDto) {
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.tid = paymentHistoryDto.getTid();
        paymentInfo.totalAmount = paymentHistoryDto.getTotalAmount();
        paymentInfo.paymentMethod = paymentHistoryDto.getPaymentMethod();
        paymentInfo.paymentStatus = paymentHistoryDto.getPaymentStatus();
        paymentInfo.approvalNumber = paymentHistoryDto.getApprovalNumber();
        paymentInfo.paymentDateTime = paymentHistoryDto.getPaymentDateTime();
        paymentInfo.cardNumberMasked = paymentHistoryDto.getCardNumberMasked();
        paymentInfo.cardIssuerCode = paymentHistoryDto.getCardIssuerCode();
        paymentInfo.merchantId = paymentHistoryDto.getMerchantId();
        paymentInfo.taxAmount = paymentHistoryDto.getTaxAmount();
        paymentInfo.discountAmount = paymentHistoryDto.getDiscountAmount();
        return paymentInfo;
    }
}
