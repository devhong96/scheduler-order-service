package com.scheduler.orderservice.order.common.service;

import com.scheduler.orderservice.order.common.domain.OrderItems;
import com.scheduler.orderservice.order.common.domain.Orders;
import com.scheduler.orderservice.order.common.domain.Vendor;
import com.scheduler.orderservice.order.common.dto.CancelOrderRequest;
import com.scheduler.orderservice.order.common.event.CancelOrderPayload;
import com.scheduler.orderservice.order.common.repository.OrderItemJpaRepository;
import com.scheduler.orderservice.order.common.repository.OrdersJpaRepository;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.scheduler.orderservice.order.client.dto.MemberFeignDto.StudentResponse;
import static com.scheduler.orderservice.order.common.dto.CancelOrderRequest.SingleCancelOrder;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderTransactionService {

    private final OrderItemJpaRepository orderItemsJpaRepository;
    private final OrdersJpaRepository ordersJpaRepository;

    @Transactional
    public CancelOrderPayload cancelOrderEvent(
            String orderId, StudentResponse studentInfo, CancelOrderRequest cancelOrderRequest
    ) {

        Orders orders = ordersJpaRepository.findOrdersByOrderId(orderId)
                .orElseThrow(EntityExistsException::new);

        List<SingleCancelOrder> singleCancelOrders = cancelOrderRequest.getSingleCancelOrders();

        List<OrderItems> ordersList = new ArrayList<>();

        for (SingleCancelOrder singleCancelOrder : singleCancelOrders) {

            ordersList.addAll(orderItemsJpaRepository
                    .findOrderItemsByOrderIdAndProductId(orderId, singleCancelOrder.getProductId()));
        }

        if (ordersList.isEmpty()) {
            throw new RuntimeException("주문을 찾을 수 없습니다.");
        }

        for (OrderItems orderItems : ordersList) {
            orderItems.cancel();
        }
        
        Vendor vendor = orders.getVendor();

        //TODO amountToCancel, taxToCancel

        // 1. 빠른 조회를 위해 취소할 productId 목록을 Set으로 만듭니다.
        Set<String> productIdsToCancel = cancelOrderRequest.getSingleCancelOrders().stream()
                .map(SingleCancelOrder::getProductId)
                .collect(Collectors.toSet());

        // 2. DB에서 가져온 주문 목록에서 취소할 productId를 가진 주문만 필터링하고 합산합니다.
        int amountToCancel = 0;

        int taxToCancel = 0;

        // 사용자가 요청한 취소 금액의 합계 (검증용)
        int requestedCancelAmount = cancelOrderRequest.getSingleCancelOrders().stream()
                .mapToInt(SingleCancelOrder::getCancelAmount)
                .sum();

        // DB 기준 실제 취소 금액과 사용자가 요청한 취소 금액이 다를 경우를 검증할 수 있습니다.
        if (requestedCancelAmount != amountToCancel) {
            // throw new RuntimeException("요청된 취소 금액과 실제 주문 금액이 일치하지 않습니다.");
        }


        return new CancelOrderPayload(vendor, orderId, studentInfo.getUsername(),
                cancelOrderRequest.getRefundReason(), amountToCancel, taxToCancel);
    }

}

