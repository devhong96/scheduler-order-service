package com.scheduler.orderservice.cart.domain;

import com.scheduler.orderservice.cart.infra.exception.InvalidCostException;
import com.scheduler.orderservice.cart.infra.exception.InvalidQuantityException;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

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

    private String studentId;

    @Enumerated(STRING)
    private OrderCategory orderCategory;

    private String image;

    private String productName;

    private Integer cost;

    private Integer quantity;

    private Integer totalPrice;

    private Boolean checked;

    public static Cart create(String studentId, OrderCategory orderCategory, String image,
                              String name, Integer cost, Integer count
    ) {
        validateCost(cost);
        validateCount(count);

        Cart cart = new Cart();
        cart.studentId = studentId;
        cart.orderCategory = orderCategory;
        cart.image = image;
        cart.productName = name;
        cart.cost = cost;
        cart.quantity = count;
        cart.totalPrice = cost * count;
        cart.checked = false;

        return cart;
    }

    public void updateChecked(Boolean checked) {
        this.checked = checked;
    }

    public void updateCount(int count) {
        if (count < MIN_COUNT) {
            throw new InvalidQuantityException("최소 수량은 " + MIN_COUNT + "개 이상입니다.");
        }
        this.quantity = count;
        updateTotalPrice();
    }

    private static void validateCost(Integer cost) {
        if (cost < MIN_COST) {
            throw new InvalidCostException("가격은 " + MIN_COST + "원 이상이어야 합니다.");
        }
    }

    private static void validateCount(Integer count) {
        if (count < MIN_COUNT) {
            throw new InvalidQuantityException("수량은 " + MIN_COUNT + "개 이상이여야 합니다.");
        }
    }

    private void updateTotalPrice() {
        this.totalPrice = this.cost * this.quantity;
    }


}


