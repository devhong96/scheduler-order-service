package com.scheduler.orderservice.order.payment.kakao.service.component;

import com.scheduler.orderservice.order.common.component.KakaoProperties;
import com.scheduler.orderservice.order.common.component.RedisOrderCache;
import com.scheduler.orderservice.order.common.dto.KakaoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.scheduler.orderservice.order.payment.kakao.dto.KakaoPayRequest.KakaoOrderApproveRequest;
import static com.scheduler.orderservice.order.payment.kakao.dto.KakaoPayResponse.KakaoApproveOrderResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApproveKakaoOrder {

    private final WebClient webClient;
    private final RedisOrderCache redisOrderCache;
    private final KakaoProperties kakaoProperties;
    private final KakaoHeaders kakaoHeaders;

    public Mono<KakaoApproveOrderResponse> kakaoApproveOrderResponse(String orderId, String pgToken) {

        KakaoDto kakaoOrder = redisOrderCache.getKakaoOrderInfo(orderId);

        String tid = kakaoOrder.getTid();

        KakaoOrderApproveRequest request = KakaoOrderApproveRequest.builder()
                .tid(tid)
                .cid(kakaoProperties.getKakaoClient().getCid())
                .partnerOrderId(orderId)
                .partnerUserId(kakaoProperties.getKakaoClient().getPartnerUserId())
                .pgToken(pgToken)
                .build();

        return webClient.post()
                .uri(kakaoProperties.getKakaoUrl().getApproveUrl())
                .headers(header -> header.addAll(kakaoHeaders.getKakaoHeaders()))
                .bodyValue(request)
                .retrieve()
                .bodyToMono(KakaoApproveOrderResponse.class)
                .doOnNext(response -> log.info("Kakao API 응답 성공: {}", response))
                .doOnError(error -> log.error("Kakao API 호출 실패: {}", error.getMessage(), error));
    }
}
