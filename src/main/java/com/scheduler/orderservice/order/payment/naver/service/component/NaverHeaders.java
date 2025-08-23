package com.scheduler.orderservice.order.payment.naver.service.component;

import com.scheduler.orderservice.order.common.component.NaverProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

@Slf4j
@Component
@RequiredArgsConstructor
public class NaverHeaders {

    private final NaverProperties properties;
    private HttpHeaders baseHeaders;

    @PostConstruct
    void initHeaders() {
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(APPLICATION_FORM_URLENCODED);

        // 발급된 client id
        headers.set(properties.getNaverHeader().getClientId(), properties.getNaverClient().getClientId());

        // 발급된 client secret
        headers.set(properties.getNaverHeader().getClientSecret(), properties.getNaverClient().getClientSecret());

        // 발급된 chain id
        headers.set(properties.getNaverHeader().getChainId(), properties.getNaverClient().getChainId());

        this.baseHeaders = HttpHeaders.readOnlyHttpHeaders(headers);
    }

    protected HttpHeaders getNaverHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(baseHeaders);

        //API 멱등성 키
        headers.set(properties.getNaverHeader().getIdempotencyKey(), UUID.randomUUID().toString());

        return headers;
    }
}
