package com.scheduler.orderservice.infra.exception;

import com.scheduler.orderservice.infra.exception.custom.AuthorityException;
import com.scheduler.orderservice.infra.exception.custom.InvalidAuthNumException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RestControllerAdvice
public class OrderException {

    @ExceptionHandler(AuthorityException.class)
    public ResponseEntity<String> handleAuthorityException() {
        return new ResponseEntity<>("do not have permission.", FORBIDDEN);
    }

    @ExceptionHandler(InvalidAuthNumException.class)
    public ResponseEntity<String> handleInvalidAuthNumException() {
        return new ResponseEntity<>("invalid AuthNUm", FORBIDDEN);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException e) {
        return new ResponseEntity<>(e.getMessage(), NOT_FOUND);
    }
}
