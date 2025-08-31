package com.scheduler.orderservice.cart.service;

import com.scheduler.orderservice.cart.dto.CartResponse;

import static com.scheduler.orderservice.cart.dto.CartRequest.*;

public interface CartService {

    CartResponse createCart(String accessToken, CartCreateRequest createRequest);

    CartResponse updateCart(String accessToken, UpdateQuantityRequest quantityRequest);

    CartResponse checkedCart(String accessToken, UpdateCheckedRequest checkedRequest);

    Long deleteCart(String accessToken, DeleteCartRequest deleteCartRequest);

}
