package com.scheduler.orderservice.infra.exception.custom;

public class UnsupportedPgException extends RuntimeException {

    public UnsupportedPgException() {}

    public UnsupportedPgException(String message) {
        super(message);
    }

    public UnsupportedPgException(String message, Throwable cause) {
        super(message, cause);
    }
    public UnsupportedPgException(Throwable cause) {
        super(cause);
    }
}
