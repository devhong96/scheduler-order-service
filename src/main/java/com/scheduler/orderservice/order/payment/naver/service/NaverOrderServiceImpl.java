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
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Objects;
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
            OrderType orderType, OrderCategory orderCategory,
            String orderId,
            String resultCode, String paymentId
    ) {

        DirectOrderDto directOrder = redisOrderCache.getDirectOrderInfo(orderId);

        StudentResponse studentInfo = memberServiceClient.getStudentInfo(directOrder.getAccessToken());
        log.info("StudentResponse at Naver OrderService = {}", studentInfo.toString());

        String studentId = studentInfo.getStudentId();
        String username = studentInfo.getUsername();

        NaverOrderResponse response = getNaverOrderResponse(resultCode, paymentId)
                .blockOptional().orElseThrow(PaymentException::new);
        log.info("NaverOrderResponse at Naver OrderService = {}", response.toString());

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



    private Mono<NaverOrderResponse> getNaverOrderResponse(String resultCode, String paymentId) {

        if(!resultCode.equals(Success.toString())) {
            throw new RuntimeException("");
        }

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("paymentId", paymentId);

        return webClient.post()
                .uri(properties.getNaverUrl().getBaseUrl() + "/" +
                        properties.getNaverClient().getPartnerId() + "/" +
                        properties.getNaverUrl().getApplyUrl())
                .headers(header -> header.addAll(getHeaders()))
                .body(BodyInserters.fromFormData(requestBody))
                .retrieve()
                .bodyToMono(NaverOrderResponse.class)
                .timeout(Duration.ofSeconds(10))
                .doOnNext(response -> log.info("Naver API 응답 성공: {}", response))
                .doOnError(error -> log.error("Naver API 호출 실패: {}", error.getMessage(), error));
    }

    @Override
    @Transactional
    public NaverCancelOrderResponse cancelNaverOrder(
            CancelNaverOrderDto cancelNaverOrderDto
    ) {
        return cancelNaverOrders(cancelNaverOrderDto).blockOptional().orElseThrow(PaymentException::new);
    }

    public Mono<NaverCancelOrderResponse> cancelNaverOrders(CancelNaverOrderDto cancelNaverOrderDto) {

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("paymentId", cancelNaverOrderDto.getPaymentId());
        requestBody.add("cancelAmount", String.valueOf(cancelNaverOrderDto.getCancelAmount()));
        requestBody.add("cancelReason", cancelNaverOrderDto.getCancelReason());
        requestBody.add("cancelRequester", cancelNaverOrderDto.getCancelRequester());
        requestBody.add("taxScopeAmount", String.valueOf(cancelNaverOrderDto.getTaxScopeAmount()));
        requestBody.add("taxExScopeAmount", String.valueOf(cancelNaverOrderDto.getTaxExScopeAmount()));

        return webClient.post()
                .uri(properties.getNaverUrl().getBaseUrl() + "/" +
                        properties.getNaverClient().getClientId() +
                        properties.getNaverUrl().getCancelUrl())
                .headers(header -> header.addAll(getHeaders()))
                .body(BodyInserters.fromFormData(requestBody))
                .retrieve()
                .bodyToMono(NaverCancelOrderResponse.class)
                .doOnNext(response -> log.info("Naver API 응답 성공: {}", response))
                .doOnError(error -> log.error("Naver API 호출 실패: {}", error.getMessage(), error));
    }

    public SearchNaverOrderHistoryResponse searchNaverOrderHistory (
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

        return responseMono.blockOptional().orElseThrow(PaymentException::new);
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(APPLICATION_FORM_URLENCODED);

        // 발급된 client id
        headers.set(properties.getNaverHeader().getClientId(),
                properties.getNaverClient().getClientId());

        // 발급된 client secret
        headers.set(properties.getNaverHeader().getClientSecret(),
                properties.getNaverClient().getClientSecret());

        // 발급된 chain id
        headers.set(properties.getNaverHeader().getChainId(),
                properties.getNaverClient().getChainId());

        //API 멱등성 키
        headers.set(properties.getNaverHeader().getIdempotencyKey(),
                UUID.randomUUID().toString());

        return headers;
    }
}
