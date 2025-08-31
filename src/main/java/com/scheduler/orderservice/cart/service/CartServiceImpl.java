package com.scheduler.orderservice.cart.service;

import com.scheduler.orderservice.cart.domain.Cart;
import com.scheduler.orderservice.cart.dto.CartResponse;
import com.scheduler.orderservice.cart.repository.CartJpaRepository;
import com.scheduler.orderservice.order.client.MemberServiceClient;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.scheduler.orderservice.cart.dto.CartRequest.*;
import static com.scheduler.orderservice.order.client.dto.MemberFeignDto.StudentResponse;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartJpaRepository cartJpaRepository;
    private final MemberServiceClient memberServiceClient;

    @Override
    @Transactional
    public CartResponse createCart(String accessToken, CartCreateRequest createRequest) {
        StudentResponse studentInfo = memberServiceClient.getStudentInfo(accessToken);

        return null;
    }

    @Override
    @Transactional
    public CartResponse updateCart(String accessToken, UpdateQuantityRequest quantityRequest) {
        StudentResponse studentInfo = memberServiceClient.getStudentInfo(accessToken);

        Cart cart = cartJpaRepository.findCartByCartIdAndStudentId(quantityRequest.getCartId(), studentInfo.getStudentId())
                .orElseThrow(EntityExistsException::new);

        cart.updateCount(quantityRequest.getQuantity());

        return null;
    }

    @Override
    @Transactional
    public CartResponse checkedCart(String accessToken, UpdateCheckedRequest checkedRequest) {
        StudentResponse studentInfo = memberServiceClient.getStudentInfo(accessToken);

        Cart cart = cartJpaRepository.findCartByCartIdAndStudentId(checkedRequest.getCartId(), studentInfo.getStudentId())
                .orElseThrow(EntityExistsException::new);

        cart.updateChecked(checkedRequest.getChecked());

        return null;
    }

    @Override
    @Transactional
    public Long deleteCart(String accessToken, DeleteCartRequest deleteCartRequest) {
        StudentResponse studentInfo = memberServiceClient.getStudentInfo(accessToken);

        return cartJpaRepository.deleteCartsByCartIdAndStudentId(
                deleteCartRequest.getCartId(), studentInfo.getStudentId());

    }
}
