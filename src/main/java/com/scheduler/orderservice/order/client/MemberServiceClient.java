package com.scheduler.orderservice.order.client;

import com.scheduler.orderservice.order.client.error.OrderFeignErrorDecoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import static com.scheduler.orderservice.order.client.dto.MemberFeignDto.StudentResponse;
import static com.scheduler.orderservice.order.client.dto.OrderDto.*;
import static org.springframework.cloud.config.client.ConfigClientProperties.AUTHORIZATION;

@FeignClient(
        name = "scheduler-member-service",
        url =  "${scheduler_member_service_url:}",
        path = "/feign-member",
        configuration = OrderFeignErrorDecoder.class
)
public interface MemberServiceClient {

    @GetMapping("student/info")
    StudentResponse getStudentInfo(@RequestHeader(AUTHORIZATION) String accessToken);

    @GetMapping("student/{username}")
    StudentResponse findStudentByUsername(@PathVariable String username);

    //
    @PostMapping("student/kakao")
    void createKakaoDirectOrder(@RequestBody CreateKakaoDirectOrderDto createKakaoDirectOrderDto);

    @PostMapping("student/naver")
    void createNaverDirectOrder(@RequestBody CreateNaverDirectOrderDto createNaverDirectOrderDto);

    @PostMapping("student/nicepay")
    void createNicePayDirectOrder(@RequestBody CreateNicePayDirectOrderDto createNicePayDirectOrderDto);


    @GetMapping("student/{readerId}/{orderId}/{ebookId}")
    CancelOrderInfoResponse findPreCancelOrderInfo(
            @PathVariable String readerId,
            @PathVariable String orderId,
            @PathVariable String ebookId
    );
}


