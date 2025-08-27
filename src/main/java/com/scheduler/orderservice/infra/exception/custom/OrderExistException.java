package com.scheduler.orderservice.infra.exception.custom;

public class OrderExistException extends RuntimeException {

    public OrderExistException() {}

    public OrderExistException(String message) {
        super(message);
    }

    public OrderExistException(String message, Throwable cause) {
        super(message, cause);
    }
    public OrderExistException(Throwable cause) {
        super(cause);
    }
}
