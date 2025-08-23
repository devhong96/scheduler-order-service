package com.scheduler.orderservice.order.payment.naver.service.component;

import com.scheduler.orderservice.infra.exception.custom.PaymentException;
import com.scheduler.orderservice.order.common.component.NaverProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.scheduler.orderservice.order.payment.naver.domain.NaverOrderResponse.Success;
import static com.scheduler.orderservice.order.payment.naver.dto.NaverPayResponse.NaverOrderResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateNaverOrder {

    private final WebClient webClient;
    private final NaverProperties properties;
    private final NaverHeaders naverHeaders;

    public Mono<NaverOrderResponse> createNaverOrderResponse(String resultCode, String paymentId) {

        if (!resultCode.equals(Success.toString())) {
            throw new PaymentException("네이버 결제 승인 실패: resultCode=" + resultCode);
        }

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("paymentId", paymentId);

        return webClient.post()
                .uri(properties.getNaverUrl().getBaseUrl() + "/" +
                        properties.getNaverClient().getPartnerId() + "/" +
                        properties.getNaverUrl().getApplyUrl())
                .headers(header -> header.addAll(naverHeaders.getNaverHeaders()))
                .body(BodyInserters.fromFormData(requestBody))
                .retrieve()
                .bodyToMono(NaverOrderResponse.class)
                .doOnNext(response -> log.info("Naver API 응답 성공: {}", response))
                .doOnError(error -> log.error("Naver API 호출 실패: {}", error.getMessage(), error));
    }

}
