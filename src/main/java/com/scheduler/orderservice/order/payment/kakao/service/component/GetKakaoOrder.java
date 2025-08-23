package com.scheduler.orderservice.order.payment.kakao.service.component;

import com.scheduler.orderservice.order.common.component.KakaoProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.scheduler.orderservice.order.payment.kakao.dto.KakaoPayRequest.KakaoOrderSearchRequest;
import static com.scheduler.orderservice.order.payment.kakao.dto.KakaoSearchOrderDto.KakaoSearchOrderResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetKakaoOrder {

    private final WebClient webClient;
    private final KakaoProperties kakaoProperties;
    private final KakaoHeaders kakaoHeaders;

    public Mono<KakaoSearchOrderResponse> getKakaoOrderResponse(String tid) {

        KakaoOrderSearchRequest orderSearchRequest = KakaoOrderSearchRequest
                .builder()
                .cid(kakaoProperties.getKakaoClient().getCid())
                .tid(tid)
                .build();

        return webClient.post()
                .uri(kakaoProperties.getKakaoUrl().getOrderUrl())
                .headers(header -> header.addAll(kakaoHeaders.getKakaoHeaders()))
                .bodyValue(orderSearchRequest)
                .retrieve()
                .bodyToMono(KakaoSearchOrderResponse.class)
                .doOnNext(response -> log.info("Kakao API 응답 성공: {}", response))
                .doOnError(error -> log.error("Kakao API 호출 실패: {}", error.getMessage(), error));
    }
}
