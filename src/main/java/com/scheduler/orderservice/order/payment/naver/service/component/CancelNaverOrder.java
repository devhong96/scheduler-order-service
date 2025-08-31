package com.scheduler.orderservice.order.payment.naver.service.component;

import com.scheduler.orderservice.order.common.component.NaverProperties;
import com.scheduler.orderservice.order.common.event.CancelOrderPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.scheduler.orderservice.order.payment.naver.dto.NaverPayResponse.NaverCancelOrderResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class CancelNaverOrder {

    private final WebClient webClient;
    private final NaverProperties properties;
    private final NaverHeaders naverHeaders;

    public Mono<NaverCancelOrderResponse> cancelNaverOrderResponse(CancelOrderPayload payload) {

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("paymentId", payload.getVendorTid());
        requestBody.add("cancelAmount", String.valueOf(payload.getCancelAmount()));
        requestBody.add("cancelReason", payload.getCancelReason());
        requestBody.add("cancelRequester", payload.getUsername());
        requestBody.add("taxScopeAmount", String.valueOf(payload.getTaxAmount()));
        requestBody.add("taxExScopeAmount", String.valueOf(0));

        return webClient.post()
                .uri(properties.getNaverUrl().getBaseUrl() + "/" +
                        properties.getNaverClient().getClientId() +
                        properties.getNaverUrl().getCancelUrl())
                .headers(header -> header.addAll(naverHeaders.getNaverHeaders()))
                .body(BodyInserters.fromFormData(requestBody))
                .retrieve()
                .bodyToMono(NaverCancelOrderResponse.class)
                .doOnNext(response -> log.info("Naver API 응답 성공: {}", response))
                .doOnError(error -> log.error("Naver API 호출 실패: {}", error.getMessage(), error));
    }
}
