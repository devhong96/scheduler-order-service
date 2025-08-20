package com.scheduler.orderservice.order.payment.nicepay.controller;

import com.scheduler.orderservice.order.common.domain.OrderCategory;
import com.scheduler.orderservice.order.common.domain.OrderType;
import com.scheduler.orderservice.order.payment.nicepay.service.NicePayService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.scheduler.orderservice.order.payment.nicepay.dto.NicePayRequest.NicePayCancelOrderRequest;
import static com.scheduler.orderservice.order.payment.nicepay.dto.NicePayRequest.NicePayPreOrderRequest;
import static com.scheduler.orderservice.order.payment.nicepay.dto.NicePayResponse.NicePayCancelOrderResponse;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/order/nicepay")
@RequiredArgsConstructor
public class NicePayOrderController {

    private final NicePayService nicePayService;

    @Operation(summary = " 페이. 바로 결제, 장바구니 결제 포함. 프론트 사용 X")
    @PostMapping("{orderType}/{orderCategory}/{orderId}")
    public ResponseEntity<Void> createNicePayOrder(
            @PathVariable String orderType,
            @PathVariable String orderCategory,
            @PathVariable String orderId,
            NicePayPreOrderRequest nicePayPreOrderRequest
    ) {
        nicePayService.createNicePayOrder(
                orderId,
                OrderType.fromString(orderType), OrderCategory.fromString(orderCategory),
                nicePayPreOrderRequest);
        return new ResponseEntity<>(CREATED);
    }

    @Operation(summary = " ")
    @PostMapping("cancel")
    public ResponseEntity<NicePayCancelOrderResponse> cancelNicePayOrder(
            @RequestBody NicePayCancelOrderRequest nicePayCancelOrderRequest
    ) {
        return new ResponseEntity<>(nicePayService.cancelNicepayOrder(nicePayCancelOrderRequest), OK);
    }
}
