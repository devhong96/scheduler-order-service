package com.scheduler.orderservice.order.payment.naver.service.component;

import com.scheduler.orderservice.order.common.component.NaverProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.scheduler.orderservice.order.payment.naver.dto.NaverPayRequest.SearchNaverOrderHistoryDto;
import static com.scheduler.orderservice.order.payment.naver.dto.NaverPayResponse.SearchNaverOrderResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class SearchNaverOrder {

    private final WebClient webClient;
    private final NaverProperties properties;
    private final NaverHeaders naverHeaders;

    public Mono<SearchNaverOrderResponse> searchNaverOrder(
            String paymentId, SearchNaverOrderHistoryDto searchHistory
    ) {
        return webClient.post()
                .uri(properties.getNaverUrl().getBaseUrl() + "/" +
                        properties.getNaverClient().getPartnerId() + "/" +
                        properties.getNaverUrl().getHistoryUrl() + "/" + paymentId)
                .headers(header -> header.addAll(naverHeaders.getNaverHeaders()))
                .bodyValue(searchHistory)
                .retrieve()
                .bodyToMono(SearchNaverOrderResponse.class)
                .doOnNext(response -> log.info("Naver API 응답 성공: {}", response))
                .doOnError(error -> log.error("Naver API 호출 실패: {}", error.getMessage(), error));
    }
}
