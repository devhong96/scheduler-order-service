package com.scheduler.orderservice.order.common.repository;

import com.scheduler.orderservice.order.common.domain.PaymentInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaRepository extends JpaRepository<PaymentInfo, Long> {
}
