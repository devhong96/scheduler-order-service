package com.scheduler.orderservice.order.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DirectOrderDto {

    private String accessToken;
    private String productId;
    private Integer quantity;

}
