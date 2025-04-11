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

    private NiceClient niceClient;
    private NiceUrl niceUrl;

    @Getter
    @Setter
    public static class NiceClient {
        private String clientKey;
        private String secretKey;
    }

    @Getter
    @Setter
    public static class NiceUrl {
        private String paymentUrl;
        private String checkAmountUrl;
        private String baseUrl;

    }
}
