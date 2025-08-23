package com.scheduler.orderservice.order.payment.nicepay.service.component;

import com.scheduler.orderservice.order.common.component.NicePayProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import static com.scheduler.orderservice.order.payment.nicepay.dto.NicePayRequest.NicePayPreOrderRequest;
import static com.scheduler.orderservice.order.payment.nicepay.dto.NicePayResponse.NicePayOrderResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateNicePayOrder {

    private final WebClient webClient;
    private final NicePayProperties properties;
    private final NicePayHeaders nicePayHeaders;

    public Mono<NicePayOrderResponse> createNicePayOrder(NicePayPreOrderRequest nicePayPreOrderRequest) {
        String tid = nicePayPreOrderRequest.getTid();

        Map<String, Integer> requestBody = new HashMap<>();
        requestBody.put("amount", nicePayPreOrderRequest.getAmount());

        return webClient.post()
                .uri(properties.getNiceUrl().getPaymentUrl() + "/" + tid)
                .headers(header -> header.addAll(nicePayHeaders.getNicePayHeaders()))
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(NicePayOrderResponse.class)
                .doOnNext(response -> log.info("Naver API 응답 성공: {}", response))
                .doOnError(error -> log.error("Naver API 호출 실패: {}", error.getMessage(), error));
    }
}
