package com.scheduler.orderservice.order.common.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@RequiredArgsConstructor(access = PROTECTED)
public class Orders extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String orderId;

    @Column(nullable = false)
    private String memberId;

    @Column(nullable = false)
    private String vendorTid;

    @Enumerated(STRING)
    private OrderCategory orderCategory;

    @Enumerated(STRING)
    private Vendor vendor;

    @Enumerated(STRING)
    private OrderType orderType;

    @Enumerated(STRING)
    private OrderStatus orderStatus;

    @Embedded
    private PaymentInfo paymentInfo;

    @Embedded
    private CancellationInfo cancellationInfo;

}
