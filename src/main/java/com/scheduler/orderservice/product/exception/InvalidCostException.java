package com.scheduler.orderservice.product.exception;

public class InvalidCostException extends RuntimeException {
    public InvalidCostException() {}
    public InvalidCostException(String message) {
        super(message);
    }

    public InvalidCostException(String message, Throwable cause) {
        super(message, cause);
    }
    public InvalidCostException(Throwable cause) {
        super(cause);
    }
}
