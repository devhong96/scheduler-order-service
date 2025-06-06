package com.scheduler.orderservice.order.payment.naver.controller;

import com.scheduler.orderservice.order.common.domain.OrderCategory;
import com.scheduler.orderservice.order.common.domain.OrderType;
import com.scheduler.orderservice.order.payment.naver.service.NaverOrderService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.scheduler.orderservice.order.payment.naver.dto.NaverPayResponse.NaverOrderResponse;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/order/naver/pay")
@RequiredArgsConstructor
public class NaverOrderController {

    private final NaverOrderService naverOrderService;

    @Operation(summary = "네이버 페이. 바로 결제, 장바구니 결제 포함. 프론트 사용 X")
    @GetMapping("{orderType}/{orderCategory}/{orderId}")
    public ResponseEntity<NaverOrderResponse> createNaverOrder(
            @PathVariable String orderId,
            @PathVariable String orderType,
            @PathVariable String orderCategory,
            @RequestParam String resultCode,
            @RequestParam String paymentId

    ) {
        return new ResponseEntity<>(naverOrderService.createNaverOrder(
                orderId, OrderType.fromString(orderType), OrderCategory.fromString(orderCategory),
                resultCode, paymentId), CREATED);
    }
}
