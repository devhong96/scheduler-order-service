package com.scheduler.orderservice.order.client;

import com.scheduler.orderservice.order.client.error.OrderFeignErrorDecoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import static com.scheduler.orderservice.order.client.dto.MemberFeignDto.StudentResponse;
import static com.scheduler.orderservice.order.client.dto.OrderDto.*;
import static org.springframework.cloud.config.client.ConfigClientProperties.AUTHORIZATION;

@FeignClient(
        name = "scheduler-order-service",
        url =  "${scheduler_order_service_url:}",
        path = "/feign-order",
        configuration = OrderFeignErrorDecoder.class
)
public interface MemberServiceClient {

    @GetMapping("feign-order-member/student")
    StudentResponse getStudentInfo(@RequestHeader(AUTHORIZATION) String accessToken);

    @GetMapping("feign-order-member/student/{username}")
    StudentResponse findReaderByUsername(@PathVariable String username);

    //
    @PostMapping("feign-order-member/student/kakao")
    void createKakaoDirectOrder(@RequestBody CreateKakaoDirectOrderDto createKakaoDirectOrderDto);

    @PostMapping("feign-order-member/student/naver")
    void createNaverDirectOrder(@RequestBody CreateNaverDirectOrderDto createNaverDirectOrderDto);

    @PostMapping("feign-order-member/student/nicepay")
    void createNicePayDirectOrder(@RequestBody CreateNicePayDirectOrderDto createNicePayDirectOrderDto);


    @GetMapping("feign-order-member/student/{readerId}/{orderId}/{ebookId}")
    CancelOrderInfoResponse findPreCancelOrderInfo(
            @PathVariable String readerId,
            @PathVariable String orderId,
            @PathVariable String ebookId
    );
}


