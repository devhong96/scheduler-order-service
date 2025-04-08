package com.scheduler.orderservice.order.common.component;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "naver")
@Getter
@Setter
public class NaverProperties {
    private String baseUrl;
    private String partnerId;
    private String clientId;
    private String clientSecret;
    private String chainId;
    private String returnUrl;
}

