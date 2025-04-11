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

    private NaverUrl naverUrl;
    private NaverClient naverClient;
    private NaverHeader naverHeader;

    @Getter
    @Setter
    public static class NaverUrl {
        private String applyUrl;
        private String cancelUrl;
        private String historyUrl;
        private String purchaseConfirmUrl;
        private String baseUrl;
    }

    @Getter
    @Setter
    public static class NaverClient {
        private String clientId;
        private String clientSecret;
        private String chainId;
        private String partnerId;
    }

    @Getter
    @Setter
    public static class NaverHeader {

        private String clientId;
        private String clientSecret;
        private String chainId;
        private String idempotencyKey;

    }
}

