package com.scheduler.orderservice.order.payment.kakao.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.scheduler.orderservice.IntegrationTest;
import com.scheduler.orderservice.order.client.MemberServiceClient;
import com.scheduler.orderservice.order.client.dto.MemberFeignDto;
import com.scheduler.orderservice.order.common.domain.OrderCategory;
import com.scheduler.orderservice.order.common.domain.OrderType;
import com.scheduler.orderservice.order.payment.kakao.dto.KakaoPayRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static com.scheduler.orderservice.order.client.dto.MemberFeignDto.*;
import static com.scheduler.orderservice.order.common.domain.OrderCategory.*;
import static com.scheduler.orderservice.order.payment.kakao.dto.KakaoPayRequest.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@IntegrationTest
class KakaoOrderServiceTest {

    @Autowired
    private KakaoOrderService kakaoOrderService;

    @MockitoBean
    private MemberServiceClient memberServiceClient;

    @Autowired
    private WireMockServer wireMockServer;

    @BeforeEach
    void startWireMockServer() {
        if (!wireMockServer.isRunning()) {
            wireMockServer.start();
        }
    }

    @AfterEach
    void stopWireMockServer() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    @Test
    @DisplayName("카카오 선결제")
    void kakaoPreOrder() {
        String accessToken = "test-token";

        StudentResponse studentInfo = new StudentResponse("STUDENT123", "Test Student");

        when(memberServiceClient.getStudentInfo(accessToken)).thenReturn(studentInfo);

        KakaoPreOrderRequest request = KakaoPreOrderRequest.builder()
                .itemCode("ITEM123")
                .partnerOrderId("TEST_ORDER123")
                .orderType(OrderType.DIRECT)
                .quantity(1)
                .itemName("Test Item")
                .totalAmount(1000)
                .taxFreeAmount(0)
                .vatAmount(100)
                .orderCategory(TUITION)
                .build();

        KakaoPreOrderResponse response = kakaoOrderService.kakaoPreOrder(accessToken, request);

        assertThat(response.getTid()).isNotNull();

        assertThat(response.getNextRedirectAppUrl())
                .contains("mobile-app/pg/one-time/payment");

        assertThat(response.getNextRedirectMobileUrl())
                .contains("mobile-web/pg/one-time/payment");

        assertThat(response.getNextRedirectPcUrl())
                .contains("/pc/pg/one-time/payment");
    }
}