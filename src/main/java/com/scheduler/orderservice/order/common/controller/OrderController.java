package com.scheduler.orderservice.order.common.controller;

import com.scheduler.orderservice.order.common.domain.OrderCategory;
import com.scheduler.orderservice.order.common.domain.OrderType;
import com.scheduler.orderservice.order.common.domain.Vendor;
import com.scheduler.orderservice.order.common.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.scheduler.orderservice.order.common.dto.OrderDto.PreOrderRequest;
import static com.scheduler.orderservice.order.common.dto.OrderResponseList.OrderResponse;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "주문 결제 버튼", description = "결제에 필요한 정보를 포함해서 반환")
    @PostMapping("{orderType}/{orderCategory}/{vendor}")
    public ResponseEntity<OrderResponse> directOrder(
            @RequestHeader(value = AUTHORIZATION, required = false) String accessToken,
            @PathVariable("orderType") String orderType,
            @PathVariable("orderCategory") String orderCategory,
            @PathVariable("vendor") String vendor,
            @Valid @RequestBody PreOrderRequest preOrderRequest
    ) {
        return new ResponseEntity<>(orderService.createOrder(
                accessToken,
                OrderType.fromString(orderType), OrderCategory.fromString(orderCategory), Vendor.fromString(vendor),
                preOrderRequest), OK);
    }
}