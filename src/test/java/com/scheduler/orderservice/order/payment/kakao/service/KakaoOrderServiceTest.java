package com.scheduler.orderservice.order.payment.kakao.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.scheduler.orderservice.IntegrationTest;
import com.scheduler.orderservice.order.client.MemberServiceClient;
import com.scheduler.orderservice.order.common.domain.OrderType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.scheduler.orderservice.order.client.dto.MemberFeignDto.StudentResponse;
import static com.scheduler.orderservice.order.common.domain.OrderCategory.TUITION;
import static com.scheduler.orderservice.order.payment.kakao.dto.KakaoPayRequest.KakaoPreOrderRequest;
import static com.scheduler.orderservice.order.payment.kakao.dto.KakaoPayResponse.KakaoPreOrderResponse;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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

        String expectedKakaoApiResponse = """
        {
            "next_redirect_app_url": https://.../mobile-app/pg/one-time/payment,
            "next_redirect_mobile_url": https://.../mobile-web/pg/one-time/payment,
            "next_redirect_pc_url": https://.../pc/pg/one-time/payment"
        }
        """;

        wireMockServer.stubFor(post(urlEqualTo("https://open-api.kakaopay.com/online/v1/payment/ready"))
                .withHeader("Authorization", matching("KakaoAK .*"))
                .withHeader("Content-Type", equalTo("application/x-www-form-urlencoded;charset=UTF-8"))

                .withRequestBody(containing("partner_order_id=TEST_ORDER123"))
                .withRequestBody(containing("item_name=Test Item"))
                .withRequestBody(containing("total_amount=1000"))
                .withRequestBody(containing("approval_url="))

                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(expectedKakaoApiResponse)));

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