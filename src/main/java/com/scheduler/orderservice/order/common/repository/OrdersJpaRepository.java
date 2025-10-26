package com.scheduler.orderservice.order.common.repository;

import com.scheduler.orderservice.order.common.domain.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrdersJpaRepository extends JpaRepository<Orders, Long> {

    Optional<Orders> findOrdersByOrderId(String orderId);

}
