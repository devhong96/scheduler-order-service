package com.scheduler.orderservice.order.exception;

import com.scheduler.orderservice.order.exception.custom.NicePayOrderException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@Slf4j
@RestControllerAdvice
public class OrderException {

    @ExceptionHandler(NicePayOrderException.class)
    public ResponseEntity<String> handleNicePayOrderException() {
        return new ResponseEntity<>("authCode error", FORBIDDEN);
    }
}
