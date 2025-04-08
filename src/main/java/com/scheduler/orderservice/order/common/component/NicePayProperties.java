package com.scheduler.orderservice.order.common.component;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "nicepay")
@Getter
@Setter
public class NicePayProperties {
    private String clientKey;
    private String secretKey;
    private String returnUrl;
}
