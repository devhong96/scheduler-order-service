package com.scheduler.orderservice.cart.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartDto {

    private String studentId;

    private String productId;

    private String productName;

    private Integer cost;

    private Integer quantity;

    private Integer totalPrice;

}
