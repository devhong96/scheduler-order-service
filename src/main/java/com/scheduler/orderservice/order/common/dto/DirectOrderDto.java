package com.scheduler.orderservice.order.common.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DirectOrderDto {

    private String accessToken;
    private String productId;
    private Integer quantity;

}
