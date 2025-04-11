package com.scheduler.orderservice.order.common.component;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "kakao")
@Getter
@Setter
public class KakaoProperties {
    private KakaoUrl kakaoUrl;
    private KakaoClient kakaoClient;

    @Getter
    @Setter
    public static class KakaoUrl {
        private String readyUrl;
        private String approveUrl;
        private String cancelUrl;
        private String orderUrl;
        private String baseUrl;
    }

    @Getter
    @Setter
    public static class KakaoClient {
        private String cid;
        private String partnerUserId;
        private String secretKey;
    }
}
