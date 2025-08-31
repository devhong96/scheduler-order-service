package com.scheduler.orderservice.order.common.service;

import com.scheduler.orderservice.order.common.domain.Orders;
import com.scheduler.orderservice.order.common.domain.Vendor;
import com.scheduler.orderservice.order.common.dto.CancelOrderRequest;
import com.scheduler.orderservice.order.common.event.CancelOrderPayload;
import com.scheduler.orderservice.order.common.repository.OrdersJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.scheduler.orderservice.order.client.dto.MemberFeignDto.StudentResponse;
import static com.scheduler.orderservice.order.common.dto.CancelOrderRequest.SingleCancelOrder;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderTransactionService {

    private final OrdersJpaRepository ordersJpaRepository;

    @Transactional
    public CancelOrderPayload cancelOrderEvent(
            String orderId, StudentResponse studentInfo, CancelOrderRequest cancelOrderRequest
    ) {

        List<Orders> ordersList = ordersJpaRepository.findOrdersByPaymentInfo_StudentIdAndPaymentInfo_OrderId(studentInfo.getStudentId(), orderId);

        Vendor vendor = ordersList.get(0).getVendor();

        // 1. 빠른 조회를 위해 취소할 productId 목록을 Set으로 만듭니다.
        Set<String> productIdsToCancel = cancelOrderRequest.getSingleCancelOrders().stream()
                .map(SingleCancelOrder::getProductId)
                .collect(Collectors.toSet());

        // 2. DB에서 가져온 주문 목록에서 취소할 productId를 가진 주문만 필터링하고 합산합니다.
        int amountToCancel = ordersList.stream()
                // 취소 요청 Set에 포함된 productId만 필터링합니다.
                .filter(order -> productIdsToCancel.contains(order.getProductId()))
                .mapToInt(order -> order.getPaymentInfo().getTotalAmount())
                .sum();

        int taxToCancel = ordersList.stream()
                .filter(order -> productIdsToCancel.contains(order.getProductId()))
                .mapToInt(order -> order.getPaymentInfo().getTaxAmount())
                .sum();

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

