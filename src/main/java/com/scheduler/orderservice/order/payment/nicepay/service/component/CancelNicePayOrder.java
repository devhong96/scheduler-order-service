package com.scheduler.orderservice.order.payment.nicepay.service.component;

import com.scheduler.orderservice.order.common.component.NicePayProperties;
import com.scheduler.orderservice.order.common.event.CancelOrderPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import static com.scheduler.orderservice.order.payment.nicepay.dto.NicePayResponse.NicePayCancelOrderResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class CancelNicePayOrder {

    private final WebClient webClient;
    private final NicePayProperties properties;
    private final NicePayHeaders nicePayHeaders;

    public Mono<NicePayCancelOrderResponse> cancelNicepayOrder(CancelOrderPayload payload) {

        Map<String, String> requestBody = new HashMap<>();

        requestBody.put("orderId", payload.getVendorTid());
        requestBody.put("reason", payload.getCancelReason());
        requestBody.put("cancelAmt", String.valueOf(payload.getCancelAmount()));

        return webClient.post()
                .uri(properties.getNiceUrl().getPaymentUrl() + "/" + payload.getVendorTid() + "/cancel")
                .headers(header -> header.addAll(nicePayHeaders.getNicePayHeaders()))
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(NicePayCancelOrderResponse.class)
                .doOnNext(response -> log.info("NicePay API 취소 응답 성공: {}", response))
                .doOnError(error -> log.error("NicePay API 취소 호출 실패: {}", error.getMessage(), error));
    }
}
