package com.scheduler.orderservice.cart.controller;

import com.scheduler.orderservice.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.scheduler.orderservice.cart.dto.CartRequest.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // 장바구니 추가
    @PostMapping("{orderId}")
    public ResponseEntity<Void> createCart(
            @RequestHeader(value = AUTHORIZATION) String accessToken,
            @RequestBody CartCreateRequest cartCreateRequest
    ) {
        return new ResponseEntity<>(OK);
    }


    // 수량 변경
    @PatchMapping("{cart}")
    public ResponseEntity<Void> changeCount(
            @RequestHeader(value = AUTHORIZATION) String accessToken,
            @RequestBody UpdateQuantityRequest updateQuantityRequest
    ) {
        return new ResponseEntity<>(OK);
    }


    // 장바구니 체크
    @PatchMapping("{boolean}")
    public ResponseEntity<Void> checkCart(
            @RequestHeader(value = AUTHORIZATION) String accessToken,
            @RequestBody UpdateCheckedRequest updateCheckedRequest
    ) {
        return new ResponseEntity<>(OK);
    }


    // 장바구니 삭제
    @DeleteMapping("{orderId}")
    public ResponseEntity<Void> deleteCart(
            @RequestHeader(value = AUTHORIZATION) String accessToken,
            @RequestBody DeleteCartRequest deleteCartRequest
    ) {
        return new ResponseEntity<>(OK);
    }
}
