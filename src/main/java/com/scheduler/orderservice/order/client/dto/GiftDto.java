package com.scheduler.orderservice.order.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;

public class GiftDto {

    @Getter
    @Builder
    public static class GiftCreateRequest {

        @JsonIgnore
        private String giftId;

        private String ebookId;

        private String ebookName;

        private Integer price;

        private String message;

        @JsonIgnore
        private String giftGiverReaderId;

        private String giftRecipientReaderId;
    }
}
