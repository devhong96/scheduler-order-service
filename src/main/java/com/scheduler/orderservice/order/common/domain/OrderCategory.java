package com.scheduler.orderservice.order.common.domain;

import lombok.Getter;

@Getter
public enum OrderCategory {

    TUITION("tuition"),
    PRODUCT("product");

    private final String description;

    OrderCategory(String description) {
        this.description = description;
    }

    public static OrderCategory fromString(String value) {
        try {
            return OrderCategory.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid OrderCategory : " + value + ". Allowed values are: self, gift.");
        }
    }
}
