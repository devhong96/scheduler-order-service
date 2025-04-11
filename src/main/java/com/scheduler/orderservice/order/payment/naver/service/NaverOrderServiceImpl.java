package com.scheduler.orderservice.order.payment.naver.service;

import com.scheduler.orderservice.infra.exception.custom.PaymentException;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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

    private final WebClient webClient;
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

        NaverOrderResponse response = getNaverOrderResponse(resultCode, paymentId);

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


    private NaverOrderResponse getNaverOrderResponse(String resultCode, String paymentId) {
        HttpHeaders headers = getHeaders();
        headers.set(
                properties.getNaverHeader().getClientSecret(),
                properties.getNaverClient().getClientSecret()
        );

        if(!resultCode.equals(Success.toString())) {
            throw new RuntimeException("");
        }

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("paymentId", paymentId);

        Mono<NaverOrderResponse> responseMono = webClient.post()
                .uri(properties.getNaverUrl().getBaseUrl() + "/" +
                        properties.getNaverClient().getPartnerId() + "/" +
                        properties.getNaverUrl().getApplyUrl())
                .headers(header -> header.addAll(getHeaders()))
                .bodyValue(body)
                .retrieve()
                .bodyToMono(NaverOrderResponse.class);

        return responseMono.blockOptional().orElseThrow(() -> new PaymentException(""));
    }


    @Deprecated
    public void cancelNaverOrder(CancelNaverDto cancelNaverDto) {

        HttpHeaders headers = getHeaders();

        HttpEntity<CancelNaverDto> requestEntity = new HttpEntity<>(cancelNaverDto, headers);

        new RestTemplate().postForEntity(
                properties.getNaverUrl().getBaseUrl() + "/" +
                        properties.getNaverClient().getClientId() +
                        properties.getNaverUrl().getCancelUrl(),
                requestEntity,
                NaverPreOrderResponse.class
        );
    }

    @Deprecated
    public SearchNaverOrderHistoryResponse searchEbookNaverOrderHistory(
            String paymentId,
            SearchNaverOrderHistoryDto searchHistory
    ) {

        Mono<SearchNaverOrderHistoryResponse> responseMono = webClient.post()
                .uri(properties.getNaverUrl().getBaseUrl() + "/" +
                        properties.getNaverClient().getPartnerId() + "/" +
                        properties.getNaverUrl().getHistoryUrl() + "/" + paymentId)
                .headers(header -> header.addAll(getHeaders()))
                .bodyValue(searchHistory)
                .retrieve()
                .bodyToMono(SearchNaverOrderHistoryResponse.class);

        return responseMono.blockOptional().orElseThrow(() -> new PaymentException(""));
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_FORM_URLENCODED);
        headers.set(properties.getNaverHeader().getClientId(),
                properties.getNaverClient().getClientId());
        headers.set(properties.getNaverHeader().getChainId(),
                properties.getNaverClient().getChainId());
        headers.set(properties.getNaverHeader().getIdempotencyKey(),
                UUID.randomUUID().toString());
        return headers;
    }
}
