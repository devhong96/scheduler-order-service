package com.scheduler.orderservice.order.client;

import com.scheduler.orderservice.order.client.error.OrderFeignErrorDecoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import static com.scheduler.orderservice.order.client.dto.MemberFeignDto.StudentResponse;
import static org.springframework.cloud.config.client.ConfigClientProperties.AUTHORIZATION;

@FeignClient(
        name = "scheduler-member-service",
        url =  "${scheduler_member_service_url:}",
        path = "/feign-order-member",
        configuration = OrderFeignErrorDecoder.class
)
public interface MemberServiceClient {

    @GetMapping("student/info")
    StudentResponse getStudentInfo(@RequestHeader(AUTHORIZATION) String accessToken);

    @GetMapping("student/{username}")
    StudentResponse findStudentByUsername(@PathVariable String username);

}


