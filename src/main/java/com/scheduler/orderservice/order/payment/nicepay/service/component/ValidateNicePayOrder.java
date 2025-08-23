package com.scheduler.orderservice.order.payment.nicepay.service.component;

import com.scheduler.orderservice.infra.exception.custom.NicePayOrderException;
import com.scheduler.orderservice.infra.exception.custom.PaymentException;
import com.scheduler.orderservice.order.common.component.NicePayProperties;
import com.scheduler.orderservice.order.payment.nicepay.dto.NicePayResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.scheduler.orderservice.order.payment.nicepay.dto.NicePayRequest.NicePayPreOrderRequest;

@Slf4j
@Component
@RequiredArgsConstructor
public class ValidateNicePayOrder {

    private final WebClient webClient;
    private final NicePayProperties properties;
    private final NicePayHeaders nicePayHeaders;

    public void validateNicePayOrder(NicePayPreOrderRequest nicePayPreOrderRequest) {

        String tid = nicePayPreOrderRequest.getTid();
        int amount = nicePayPreOrderRequest.getAmount();

        Map<String, Integer> requestBody = new HashMap<>();
        requestBody.put("amount", amount);

        Mono<NicePayResponse.NicePayCheckAmountResponse> responseMono = webClient.post()
                .uri(properties.getNiceUrl().getCheckAmountUrl() + "/" + tid)
                .headers(header -> header.addAll(nicePayHeaders.getNicePayHeaders()))
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(NicePayResponse.NicePayCheckAmountResponse.class);

        NicePayResponse.NicePayCheckAmountResponse Response = responseMono.blockOptional().orElseThrow(PaymentException::new);

        String resultCode = Objects.requireNonNull(Response).getResultCode();
        boolean isValid = Response.getIsValid();
        String resultTid = Response.getTid();

        if(!isValid && !resultTid.equals(tid) && !resultCode.equals("0000"))
            throw new NicePayOrderException();
    }
}
