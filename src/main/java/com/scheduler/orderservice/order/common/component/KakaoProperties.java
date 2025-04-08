package com.scheduler.orderservice.order.common.component;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "kakao.client")
@Getter
@Setter
public class KakaoProperties {
    private String baseUrl;
    private String cid;
    private String partnerUserId;
    private String secretKey;
}
