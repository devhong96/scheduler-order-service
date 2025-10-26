package com.scheduler.orderservice.order.common.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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

    private String orderId;

    @Enumerated(STRING)
    private OrderType orderType;

    @Enumerated(STRING)
    private OrderCategory orderCategory;

    private String productId;

    private String productName;

    private Integer price;

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
