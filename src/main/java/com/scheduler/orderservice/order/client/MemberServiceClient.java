package com.scheduler.orderservice.order.client;

import com.scheduler.orderservice.order.client.error.EbookFeignErrorDecoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.scheduler.orderservice.order.client.dto.GiftDto.GiftCreateRequest;
import static com.scheduler.orderservice.order.client.dto.MemberFeignDto.CreateOwnedEbookListDto;
import static com.scheduler.orderservice.order.client.dto.MemberFeignDto.StudentResponse;
import static com.scheduler.orderservice.order.client.dto.OrderDto.*;
import static org.springframework.cloud.config.client.ConfigClientProperties.AUTHORIZATION;

@FeignClient(
        name = "member-service",
        path = "/scheduler-course-service/feign-member/",
        configuration = EbookFeignErrorDecoder.class
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


    @PostMapping("feign-order-member/student/create-owned-ebook/list")
    void createOwnedEbookList(
            @RequestBody CreateOwnedEbookListDto createOwnedEbookListDto
    );
//

    @GetMapping("feign-order-member/student/{readerId}/{isOwner}")
    List<String> findOwnedEbookIdsByReaderId(
            @PathVariable String readerId,
            @PathVariable Boolean isOwner
    );

    @GetMapping("feign-order-member/student/{readerId}/{orderId}/{ebookId}")
    CancelOrderInfoResponse findPreCancelOrderInfo(
            @PathVariable String readerId,
            @PathVariable String orderId,
            @PathVariable String ebookId
    );
}


