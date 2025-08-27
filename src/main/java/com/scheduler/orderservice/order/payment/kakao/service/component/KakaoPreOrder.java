package com.scheduler.orderservice.order.payment.kakao.service.component;

import com.scheduler.orderservice.order.common.component.KakaoProperties;
import com.scheduler.orderservice.order.common.domain.OrderType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.scheduler.orderservice.order.payment.kakao.dto.KakaoPayRequest.KakaoOrderReadyRequest;
import static com.scheduler.orderservice.order.payment.kakao.dto.KakaoPayRequest.KakaoPreOrderRequest;
import static com.scheduler.orderservice.order.payment.kakao.dto.KakaoPayResponse.KakaoPreOrderResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoPreOrder {

    private final WebClient webClient;
    private final KakaoProperties kakaoProperties;
    private final KakaoHeaders kakaoHeaders;

    public Mono<KakaoPreOrderResponse> kakaoPreOrderResponse(
            String orderId, KakaoPreOrderRequest kakaoPreOrderRequest
    ) {

        OrderType orderType = kakaoPreOrderRequest.getOrderType();
        String vendorReturnUrl = kakaoProperties.getKakaoUrl().getBaseUrl();
        String orderReturnUri = "order/kakao/pay/success";

        String orderCategoryIdPath = kakaoPreOrderRequest.getOrderCategory().getDescription();

        KakaoOrderReadyRequest kakaoOrderReadyRequest = KakaoOrderReadyRequest.builder()
                .cid(kakaoProperties.getKakaoClient().getCid())
                //가맹점 주문번호
                .partnerOrderId(orderId)
                .partnerUserId(kakaoProperties.getKakaoClient().getPartnerUserId())
                .itemCode(kakaoPreOrderRequest.getItemCode())
                .itemName(kakaoPreOrderRequest.getItemName())
                .quantity(kakaoPreOrderRequest.getQuantity())
                .totalAmount(kakaoPreOrderRequest.getTotalAmount())
                .taxFreeAmount(kakaoPreOrderRequest.getTaxFreeAmount())
                .vatAmount(kakaoPreOrderRequest.getVatAmount())
                .approvalUrl(vendorReturnUrl + "/" + orderReturnUri + "/" + orderType.toString().toLowerCase() + "/" + orderCategoryIdPath + "/" + orderId)
                .cancelUrl(vendorReturnUrl + "/order/kakao/pay/cancel")
                .failUrl(vendorReturnUrl + "/order/kakao/pay/fail")
                .build();

        return webClient.post()
                .uri(kakaoProperties.getKakaoUrl().getReadyUrl())
                .headers(header -> header.addAll(kakaoHeaders.getKakaoHeaders()))
                .bodyValue(kakaoOrderReadyRequest)
                .retrieve()
                .bodyToMono(KakaoPreOrderResponse.class)
                .doOnNext(response -> log.info("Kakao API 응답 성공: {}", response))
                .doOnError(error -> log.error("Kakao API 호출 실패: {}", error.getMessage(), error));
    }
}
