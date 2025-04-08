package com.scheduler.orderservice.order.common.domain;

public enum OrderType {

    DIRECT, CART;

    public static OrderType fromString(String value) {
        try {
            return OrderType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid order type: " + value + ". Allowed values are: cart, direct.");
        }
    }
}
