package com.scheduler.orderservice.order.payment.kakao.service.component;

import com.scheduler.orderservice.order.common.component.KakaoProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoHeaders {

    private final KakaoProperties kakaoProperties;
    private HttpHeaders baseHeaders;

    @PostConstruct
    void initHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTHORIZATION, "DEV_SECRET_KEY " + kakaoProperties.getKakaoClient().getSecretKey());
        headers.setContentType(APPLICATION_JSON);
        this.baseHeaders = HttpHeaders.readOnlyHttpHeaders(headers);
    }

    protected HttpHeaders getKakaoHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(baseHeaders);
        return headers;
    }
}
