package com.scheduler.orderservice.order.payment.kakao.controller;

import com.scheduler.orderservice.order.common.domain.OrderCategory;
import com.scheduler.orderservice.order.common.domain.OrderType;
import com.scheduler.orderservice.order.payment.kakao.service.KakaoOrderService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.scheduler.orderservice.order.payment.kakao.dto.KakaoCancelOrderDto.CancelOrderPreRequest;
import static com.scheduler.orderservice.order.payment.kakao.dto.KakaoPayRequest.KakaoOrderResponse;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.BAD_GATEWAY;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/order/kakao/pay")
@RequiredArgsConstructor
public class KakaoOrderController {

    private final KakaoOrderService kakaoOrderService;

    @Operation(summary = "카카오 페이 결제", description = "프론트에서 사용 X")
    @GetMapping("success/{orderType}/{orderCategory}/{orderId}")
    public ResponseEntity<KakaoOrderResponse> createKakaoCartOrder(
            @PathVariable String orderType,
            @PathVariable String orderCategory,
            @PathVariable String orderId,
            @RequestParam(name = "pg_token") String pgToken
    ) {
        return new ResponseEntity<>(kakaoOrderService.createKakaoOrder(
                OrderType.fromString(orderType),
                OrderCategory.fromString(orderCategory),
                orderId, pgToken), OK);
    }

    @Operation(summary = "결제 실패", description = "프론트에서 사용 X")
    @PostMapping("fail")
    public ResponseEntity<Void> kakaoOrderFail() {
        return new ResponseEntity<>(BAD_GATEWAY);
    }

    @Operation(summary = "결제 취소")
    @PostMapping("cancel")
    public ResponseEntity<Void> kakaoOrderCancel(
            @RequestHeader(AUTHORIZATION) String accessToken,
            @RequestBody CancelOrderPreRequest cancelOrderPreRequest
    ) {
        kakaoOrderService.prepareToCancelKakaoOrder(accessToken, cancelOrderPreRequest);
        return new ResponseEntity<>(BAD_GATEWAY);
    }
}
