package com.scheduler.orderservice.order.payment.nicepay.service.component;

import com.scheduler.orderservice.order.common.component.NicePayProperties;
import com.scheduler.orderservice.order.payment.nicepay.dto.NicePayRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.scheduler.orderservice.order.payment.nicepay.dto.NicePayResponse.NicePayCancelOrderResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class CancelNicePayOrder {

    private final WebClient webClient;
    private final NicePayProperties properties;
    private final NicePayHeaders nicePayHeaders;

    public Mono<NicePayCancelOrderResponse> cancelNicepayOrder(NicePayRequest.NicePayCancelOrderRequest nicePayCancelOrderRequest) {

        String tid = nicePayCancelOrderRequest.getTid();
        String reason = nicePayCancelOrderRequest.getReason();
        String cancelAmt = nicePayCancelOrderRequest.getCancelAmt();

        Map<String, String> requestBody = new HashMap<>();

        requestBody.put("orderId", UUID.randomUUID().toString());
        requestBody.put("reason", reason);
        requestBody.put("cancelAmt", cancelAmt);

        return webClient.post()
                .uri(properties.getNiceUrl().getPaymentUrl() + "/" + tid + "/cancel")
                .headers(header -> header.addAll(nicePayHeaders.getNicePayHeaders()))
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(NicePayCancelOrderResponse.class)
                .doOnNext(response -> log.info("NicePay API 취소 응답 성공: {}", response))
                .doOnError(error -> log.error("NicePay API 취소 호출 실패: {}", error.getMessage(), error));
    }
}
