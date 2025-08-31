package com.scheduler.orderservice.cart.domain;

import com.scheduler.orderservice.cart.infra.exception.InvalidCostException;
import com.scheduler.orderservice.cart.infra.exception.InvalidQuantityException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@RequiredArgsConstructor(access = PROTECTED)
public class Cart extends BaseEntity {

    private static final int MIN_COUNT = 1;
    private static final int MIN_COST = 1000;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, updatable = false)
    private String cartId;

    private String studentId;

    @Enumerated(STRING)
    private OrderCategory orderCategory;

    private String image;

    private String productId;

    private String productName;

    private Integer cost;

    private Integer quantity;

    private Integer totalPrice;

    private Boolean checked;

    public static Cart create(String studentId, OrderCategory orderCategory, String image,
                              String productId, String productName, Integer cost, Integer quantity
    ) {
        validateCost(cost);
        validateCount(quantity);

        Cart cart = new Cart();
        cart.studentId = studentId;
        cart.orderCategory = orderCategory;
        cart.image = image;
        cart.productId = productId;
        cart.productName = productName;
        cart.cost = cost;
        cart.quantity = quantity;
        cart.totalPrice = cost * quantity;
        cart.checked = false;

        return cart;
    }

    public void updateChecked(Boolean checked) {
        this.checked = checked;
    }

    public void updateCount(int quantity) {
        if (quantity < MIN_COUNT) {
            throw new InvalidQuantityException("최소 수량은 " + MIN_COUNT + "개 이상입니다.");
        }
        this.quantity = quantity;
        updateTotalPrice();
    }

    private static void validateCost(Integer cost) {
        if (cost < MIN_COST) {
            throw new InvalidCostException("가격은 " + MIN_COST + "원 이상이어야 합니다.");
        }
    }

    private static void validateCount(Integer quantity) {
        if (quantity < MIN_COUNT) {
            throw new InvalidQuantityException("수량은 " + MIN_COUNT + "개 이상이여야 합니다.");
        }
    }

    private void updateTotalPrice() {
        this.totalPrice = this.cost * this.quantity;
    }

    @PrePersist
    public void createUuid() {
        this.cartId = UUID.randomUUID().toString();
    }


}


