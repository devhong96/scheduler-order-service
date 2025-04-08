package com.scheduler.orderservice.order.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.scheduler.orderservice.order.payment.naver.domain.CategoryId;
import com.scheduler.orderservice.order.payment.naver.domain.CategoryType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static jakarta.persistence.EnumType.STRING;

public class OrderDto {

    @Getter
    @Setter
    public static class OrderRequest {

        private String productName;

        private Integer price;

        private Integer quantity;

        private List<ProductItems> productItems;

        private GiftOrderInfoRequest giftOrderInfoRequest;
    }

    @Getter
    @Setter
    public static class ProductItems {

        @Enumerated(STRING)
        private CategoryType categoryType;

        @Enumerated(STRING)
        private CategoryId categoryId;

        //productId
        private String uid;

        private String name;

        private String productCoverUrl;

        private Integer count;

        private Integer donationAmount;
    }


    @Getter
    @Setter
    public static class GiftOrderInfoRequest {

        @JsonIgnore
        private String giftGiverReaderId;

        private String giftRecipientUsername;

        private String message;
    }
}
