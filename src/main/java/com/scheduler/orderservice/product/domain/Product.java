package com.scheduler.orderservice.product.domain;

import com.scheduler.orderservice.product.exception.InvalidCostException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@RequiredArgsConstructor(access = PROTECTED)
public class Product extends BaseEntity {

    private static final int MIN_COST = 1000;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Enumerated(STRING)
    private ProductCategory productType;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private Integer cost;

    @Column(nullable = false)
    private Boolean status;

    public static Product create(String productName, Integer cost) {

        validateCost(cost);
        Product product = new Product();
        product.productName = productName;
        product.cost = cost;
        product.status = false;

        return product;
    }

    public void updateStatus(Boolean status) {
        this.status = status;
    }

    private static void validateCost(Integer cost) {
        if (cost < MIN_COST) {
            throw new InvalidCostException("가격은 " + MIN_COST + "원 이상이어야 합니다.");
        }
    }
}
