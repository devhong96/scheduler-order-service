package com.scheduler.orderservice.order.payment.kakao.service.component;

import com.scheduler.orderservice.order.common.component.KakaoProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.scheduler.orderservice.order.payment.kakao.dto.KakaoCancelOrderDto.CancelOrderRequest;
import static com.scheduler.orderservice.order.payment.kakao.dto.KakaoCancelOrderDto.CancelOrderResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class CancelKakaoOrder {

    private final WebClient webClient;
    private final KakaoProperties kakaoProperties;
    private final KakaoHeaders kakaoHeaders;
    private final GetKakaoOrder getKakaoOrder;

    public Mono<CancelOrderResponse> cancelKakaoOrder(CancelOrderRequest cancelOrderRequest) {

        String tid = cancelOrderRequest.getTid();

        return getKakaoOrder.getKakaoOrderResponse(tid)
                .then(
                        webClient.post()
                                .uri(kakaoProperties.getKakaoUrl().getCancelUrl())
                                .headers(h -> h.addAll(kakaoHeaders.getKakaoHeaders()))
                                .bodyValue(cancelOrderRequest)
                                .retrieve()
                                .bodyToMono(CancelOrderResponse.class)
                                .doOnNext(response -> log.info("Kakao API 응답 성공: {}", response))
                                .doOnError(error -> log.error("Kakao API 호출 실패: {}", error.getMessage(), error))
        );
    }
}
