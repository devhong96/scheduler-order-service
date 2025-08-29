package com.scheduler.orderservice.order.common.domain;

public enum OrderStatus {
    // 결제 관련 상태
    PAYMENT_PENDING,   // 결제 대기 중
    PAYMENT_COMPLETED, // 결제 완료
    PAYMENT_FAILED,    // 결제 실패

    // 처리 관련 상태
    PREPARING,         // 상품 준비 중
    READY_FOR_SHIPMENT,// 배송 준비 완료

    // 배송 관련 상태
    SHIPPING,          // 배송 중
    DELIVERED,         // 배송 완료

    // 완료 및 취소/반품 관련 상태
    COMPLETED,         // 주문 완료 (구매 확정)
    CANCELED,          // 주문 취소
    RETURN_REQUESTED,  // 반품 요청
    RETURNED           // 반품 완료
}
