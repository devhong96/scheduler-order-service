package com.scheduler.orderservice.order.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DirectOrderDto {

    private String accessToken;
    private String productId;
    private Integer quantity;

    public DirectOrderDto(String accessToken, String productId, Integer quantity) {
        this.accessToken = accessToken;
        this.productId = productId;
        this.quantity = quantity;
    }
}
