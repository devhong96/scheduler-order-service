package com.scheduler.orderservice.order.common.domain;

public enum Vendor {

    KAKAO, NAVER, NICEPAY;

    public static Vendor fromString(String value) {
        try {
            return Vendor.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid vendor type: " + value + ". Allowed values are: kakao, naver, nicepay.");
        }
    }
}
