package com.scheduler.orderservice.cart.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.scheduler.orderservice.cart.dto.CartDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.scheduler.orderservice.cart.domain.QCart.cart;

@Repository
@RequiredArgsConstructor
public class CartRepository {

    private final JPAQueryFactory queryFactory;

    public List<CartDto> getCartDtoByStudentId(String studentId, Boolean checked) {
        return queryFactory
                .select(Projections.fields(CartDto.class,
                        cart.studentId,
                        cart.productId,
                        cart.productName,
                        cart.cost,
                        cart.quantity,
                        cart.totalPrice))
                .from(cart)
                .where(cart.studentId.eq(studentId).and(cart.checked.eq(checked)))
                .fetch();
    }

}
