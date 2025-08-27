package com.scheduler.orderservice.infra.health;

import com.scheduler.orderservice.order.client.MemberServiceClient;
import com.scheduler.orderservice.order.common.domain.OrderCategory;
import com.scheduler.orderservice.order.payment.naver.dto.NaverPayResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.scheduler.orderservice.order.client.dto.MemberFeignDto.StudentResponse;

@Service
@RequiredArgsConstructor
public class TestService {

    private final MemberServiceClient memberServiceClient;

    @Transactional
    public StudentResponse test (
            String accessToken
    ) {
        return memberServiceClient.getStudentInfo(accessToken);
    }

    @Transactional
    public void test (
            String studentId, String username, Integer quantity
    ) {

    }

    public static NaverPayResponse.NaverOrderResponse.Detail createSampleDetail() {
        NaverPayResponse.NaverOrderResponse.Detail detail = new NaverPayResponse.NaverOrderResponse.Detail();

        // --- 기본 거래 정보 ---
        detail.setProductName("스마트폰 케이스");
        detail.setMerchantName("네이버 스토어");
        detail.setMerchantId("naver_store_123");
        detail.setPaymentId("pay_20250823_xyz123abc");
        detail.setPayHistId("hist_987654321");
        detail.setAdmissionState("SUCCESS"); // 거래 성공
        detail.setAdmissionYmdt("2025-08-23T15:50:00"); // 거래 승인 시각
        detail.setTradeConfirmYmdt("2025-08-23T15:50:05"); // 거래 확정 시각
        detail.setUseCfmYmdt("2025-08-23T15:50:05"); // 사용 확정 시각

        // --- 금액 정보 ---
        detail.setTotalPayAmount(25000); // 총 결제 금액
        detail.setApplyPayAmount(22500); // 할인/포인트 적용 후 실제 결제 금액
        detail.setDiscountPayAmount(2500); // 할인 금액
        detail.setPrimaryPayAmount(20000); // 주 결제수단(카드) 결제 금액
        detail.setNpointPayAmount(2500); // N포인트 사용 금액
        detail.setGiftCardAmount(0); // 기프트카드 사용 금액
        detail.setTaxScopeAmount(22500); // 과세 대상 금액
        detail.setTaxExScopeAmount(0); // 면세 대상 금액
        detail.setEnvironmentDepositAmount(0); // 환경 부담금

        // --- 결제 수단 정보 (신용카드) ---
        detail.setPrimaryPayMeans("CARD");
        detail.setCardNo("1234-56**-****-8910");
        detail.setCardCorpCode("04"); // KB국민카드
        detail.setCardAuthNo("12345678"); // 승인번호
        detail.setCardInstCount(0); // 일시불
        detail.setUsedCardPoint(false); // 카드사 포인트 사용 안함

        // --- 정산 정보 ---
        detail.setSettleExpectAmount(21850); // 정산 예정 금액
        detail.setPayCommissionAmount(650); // 결제 수수료
        detail.setSettleExpected(true); // 정산 예정 여부

        // --- 사용자 및 기타 정보 ---
        detail.setMerchantPayKey("mpk_abcdef123456");
        detail.setMerchantUserKey("muk_user_001");
        detail.setUserIdentifier("uid_user_001");
        detail.setExtraDeduction(false); // 추가 공제 없음

        // 사용하지 않은 필드는 null 또는 기본값으로 유지됩니다.
        // detail.setBankCorpCode(null);
        // detail.setBankAccountNo(null);

        return detail;
    }
}
