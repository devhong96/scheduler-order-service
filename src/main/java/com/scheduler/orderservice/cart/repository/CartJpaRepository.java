package com.scheduler.orderservice.cart.repository;

import com.scheduler.orderservice.cart.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartJpaRepository extends JpaRepository<Cart, Long> {

    void deleteByStudentIdAndChecked(String studentId, Boolean checked);

    Long deleteCartsByCartIdAndStudentId(String cartId, String studentId);

    Optional<Cart> findCartByCartIdAndStudentId(String cartId, String studentId);

}
