package com.scheduler.orderservice.order.common.repository;

import com.scheduler.orderservice.order.common.domain.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemJpaRepository extends JpaRepository<OrderItems, Long> {

    List<OrderItems> findOrderItemsByOrderIdAndProductId(String orderId, String productId);
}
