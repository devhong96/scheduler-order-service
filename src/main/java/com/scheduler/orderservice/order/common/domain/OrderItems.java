package com.scheduler.orderservice.order.common.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.scheduler.orderservice.order.common.domain.OrderStatus.CANCELED;
import static com.scheduler.orderservice.order.common.domain.OrderStatus.PAYMENT_COMPLETED;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class OrderItems {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String orderId;

    @Enumerated(STRING)
    @Column(nullable = false)
    private OrderType orderType;

    @Enumerated(STRING)
    @Column(nullable = false)
    private OrderCategory orderCategory;

    @Column(nullable = false)
    private String productId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer quantity;

    @Enumerated(STRING)
    private OrderStatus orderStatus;

    public void cancel() {
        if (this.orderStatus == CANCELED) {
            throw new IllegalStateException("이미 취소된 주문입니다.");
        }

        this.orderStatus = CANCELED;
    }

    public static OrderItems create(String orderId, OrderType orderType, OrderCategory orderCategory,
                                String productId, String productName, Integer quantity
    ) {
        OrderItems orderItems = new OrderItems();
        orderItems.orderId = orderId;
        orderItems.orderType = orderType;
        orderItems.orderCategory = orderCategory;
        orderItems.productId = productId;
        orderItems.productName = productName;
        orderItems.quantity = quantity;
        orderItems.orderStatus = PAYMENT_COMPLETED;

        return orderItems;
    }

}
