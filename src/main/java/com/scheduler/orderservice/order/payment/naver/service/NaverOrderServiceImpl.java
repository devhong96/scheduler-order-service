package com.scheduler.orderservice.order.payment.naver.service;

import com.scheduler.orderservice.order.client.MemberServiceClient;
import com.scheduler.orderservice.order.common.component.NaverProperties;
import com.scheduler.orderservice.order.common.component.RedisOrderCache;
import com.scheduler.orderservice.order.common.domain.OrderCategory;
import com.scheduler.orderservice.order.common.domain.OrderType;
import com.scheduler.orderservice.order.common.dto.DirectOrderDto;
import com.scheduler.orderservice.order.payment.event.direct.vendor.NaverAfterDirectOrderEvent;
import com.scheduler.orderservice.order.payment.event.direct.vendor.NaverDirectOrderEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import static com.scheduler.orderservice.order.client.dto.MemberFeignDto.StudentResponse;
import static com.scheduler.orderservice.order.payment.naver.domain.NaverOrderResponse.Success;
import static com.scheduler.orderservice.order.payment.naver.dto.NaverPayRequest.SearchNaverOrderHistoryDto;
import static com.scheduler.orderservice.order.payment.naver.dto.NaverPayResponse.*;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverOrderServiceImpl implements NaverOrderService {

    private final NaverProperties properties;
    private final MemberServiceClient memberServiceClient;
    private final RedisOrderCache redisOrderCache;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public NaverOrderResponse createNaverOrder(
            String orderId,
            OrderType orderType, OrderCategory orderCategory,
            String resultCode, String paymentId
    ) {

        DirectOrderDto directOrder = redisOrderCache.getDirectOrderInfo(orderId);
        StudentResponse studentInfo = memberServiceClient.getStudentInfo(directOrder.getAccessToken());
        String studentId = studentInfo.getStudentId();
        String username = studentInfo.getUsername();

        NaverOrderResponse response = getNaverEbookOrderResponse(resultCode, paymentId);

        switch (orderType) {
            case DIRECT: {

                Integer quantity = directOrder.getQuantity();

                eventPublisher.publishEvent(new NaverDirectOrderEvent(this, studentId, username, quantity, orderCategory, response));
                eventPublisher.publishEvent(new NaverAfterDirectOrderEvent(this, studentId, username, quantity, orderCategory, response));
                break;
            }
            case CART: {

                break;
            }
        }
        return response;
    }


    private NaverOrderResponse getNaverEbookOrderResponse(String resultCode, String paymentId) {
        HttpHeaders headers = getHeaders();
        headers.set("X-Naver-Client-Secret", properties.getClientSecret());

        if(!resultCode.equals(Success.toString())) {
            throw new RuntimeException("");
        }

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("paymentId", paymentId);

        HttpEntity<MultiValueMap<String, String>> mapHttpEntity = new HttpEntity<>(body, headers);

        ResponseEntity<NaverOrderResponse> naverOrderResponse = new RestTemplate()
                .postForEntity(
                properties.getBaseUrl() + "/" + "naverpay-partner" + "/naverpay/payments/v2.2/apply/payment",
                mapHttpEntity,
                NaverOrderResponse.class
        );

        return naverOrderResponse.getBody();
    }


    @Deprecated
    public void cancelEbookNaverOrder(CancelNaverEbookDto cancelNaverEbookDto) {

        HttpHeaders headers = getHeaders();

        HttpEntity<CancelNaverEbookDto> requestEntity = new HttpEntity<>(cancelNaverEbookDto, headers);

        new RestTemplate().postForEntity(
                properties.getBaseUrl() + "/" + properties.getPartnerId() + "/naverpay/payments/v1/cancel",
                requestEntity,
                NaverPreOrderResponse.class
        );
    }

    @Deprecated
    public SearchNaverEbookOrderHistoryResponse searchEbookNaverOrderHistory(String paymentId,
                                                                             SearchNaverOrderHistoryDto searchHistory
    ) {
        HttpHeaders headers = getHeaders();

        HttpEntity<SearchNaverOrderHistoryDto> requestEntity = new HttpEntity<>(searchHistory, headers);

        ResponseEntity<SearchNaverEbookOrderHistoryResponse> response = new RestTemplate().postForEntity(
                properties.getBaseUrl() + "/" + properties.getPartnerId() + "naverpay/payments/v2.2/list/history/" + paymentId,
                requestEntity,
                SearchNaverEbookOrderHistoryResponse.class
        );

        return response.getBody();
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_FORM_URLENCODED);
        headers.set("X-Naver-Client-Id", properties.getClientId());
        headers.set("X-NaverPay-Chain-Id", properties.getChainId());
        headers.set("X-NaverPay-Idempotency-Key", UUID.randomUUID().toString());
        return headers;
    }
}
