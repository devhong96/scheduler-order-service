package com.scheduler.orderservice.order.payment.nicepay.controller;

import com.scheduler.orderservice.order.common.domain.OrderCategory;
import com.scheduler.orderservice.order.common.domain.OrderType;
import com.scheduler.orderservice.order.payment.nicepay.service.NicePayService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.scheduler.orderservice.order.payment.nicepay.dto.NicePayRequest.NicePayPreOrderRequest;
import static com.scheduler.orderservice.order.payment.nicepay.dto.NicePayResponse.NicePayOrderResponse;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/order/nicepay")
@RequiredArgsConstructor
public class NicePayOrderController {

    private final NicePayService nicePayService;

    @Operation(summary = " 페이. 바로 결제, 장바구니 결제 포함. 프론트 사용 X")
    @PostMapping("{orderType}/{orderCategory}/{orderId}")
    public ResponseEntity<NicePayOrderResponse> createNicePayOrder(
            @PathVariable String orderType,
            @PathVariable String orderCategory,
            @PathVariable String orderId,
            NicePayPreOrderRequest nicePayPreOrderRequest
    ) {
        return new ResponseEntity<>(nicePayService.createNicePayOrder(
                orderId,
                OrderType.fromString(orderType), OrderCategory.fromString(orderCategory),
                nicePayPreOrderRequest), CREATED);
    }

}
