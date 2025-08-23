package com.scheduler.orderservice.order.payment.nicepay.service.component;

import com.scheduler.orderservice.order.common.component.NicePayProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@Component
@RequiredArgsConstructor
public class NicePayHeaders {

    private final NicePayProperties properties;
    private HttpHeaders baseHeaders;

    @PostConstruct
    void initHeaders() {

        String clientKey = properties.getNiceClient().getClientKey();
        String secretKey = properties.getNiceClient().getSecretKey();
        String credentials = clientKey + ":" + secretKey;

        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes(UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        headers.set(AUTHORIZATION, "Basic " + encodedCredentials);

        this.baseHeaders = HttpHeaders.readOnlyHttpHeaders(headers);
    }

    protected HttpHeaders getNicePayHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(baseHeaders);
        return headers;
    }
}
