package com.scheduler.orderservice.order.payment.naver.domain;

import lombok.Getter;

@Getter
public enum NaverOrderResponse {

    Success("성공"),
    Fail("PG, 은행 및 기타 오류 발생 시 오류 상세 원인이 message 필드로 전달됩니다. 결제 실패 사유를 인지할 수 있도록 Alert 등을 통한 안내가 필요합니다."),
    InvalidMerchant("유효하지 않은 가맹점인 경우"),
    TimeExpired("결제 승인 가능 시간 초과 시 (10분 초과시)"),
    AlreadyOnGoing("해당 결제번호로 결제가 이미 진행 중일 때"),
    AlreadyComplete("해당 결제번호로 이미 결제가 완료되었을 때"),
    OwnerAuthFail("본인 카드 인증 오류 시"),
    BankMaintenance("충전 계좌 점검 시"),
    NotEnoughAccountBalance("충전 계좌 잔고 부족"),
    MaintenanceOngoing("서비스 점검중"),
    FaultCheckOngoing("장애 점검 중");

    private final String description;

    NaverOrderResponse(String description) {
        this.description = description;
    }
}
