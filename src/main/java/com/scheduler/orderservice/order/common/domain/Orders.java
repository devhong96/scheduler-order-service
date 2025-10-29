package com.scheduler.orderservice.order.common.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.scheduler.orderservice.order.client.dto.MemberFeignDto.StudentResponse;
import static com.scheduler.orderservice.order.common.domain.OrderStatus.CANCELED;
import static com.scheduler.orderservice.order.common.domain.OrderStatus.PAYMENT_COMPLETED;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Orders extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String studentId;

    @Column(nullable = false)
    private String orderId;

    @Column(nullable = false)
    private String vendorTid;

    @Enumerated(STRING)
    private Vendor vendor;

    @Enumerated(STRING)
    private OrderStatus orderStatus;

    @Embedded
    private CancellationInfo cancellationInfo;

    public static Orders create(String orderId, Vendor vendor, String vendorTid,
                                StudentResponse studentResponse
    ) {
        Orders orders = new Orders();
        orders.orderId = orderId;
        orders.vendor = vendor;
        orders.username = studentResponse.getUsername();
        orders.studentId = studentResponse.getStudentId();
        orders.vendorTid = vendorTid;
        orders.orderStatus = PAYMENT_COMPLETED;
        return orders;
    }

    public void cancel() {
        if (this.orderStatus == CANCELED) {
            throw new IllegalStateException("이미 취소된 주문입니다.");
        }

        this.orderStatus = CANCELED; // 1. 주문 상태를 CANCELED로 변경
        this.cancellationInfo = new CancellationInfo();
    }

}
