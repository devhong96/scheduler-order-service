package com.scheduler.orderservice.infra.exception.custom;

public class NicePayOrderException extends RuntimeException {
    public NicePayOrderException() {}
    public NicePayOrderException(String message) {
        super(message);
    }

    public NicePayOrderException(String message, Throwable cause) {
        super(message, cause);
    }
    public NicePayOrderException(Throwable cause) {
        super(cause);
    }
}


