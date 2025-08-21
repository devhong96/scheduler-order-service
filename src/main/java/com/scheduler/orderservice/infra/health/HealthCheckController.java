package com.scheduler.orderservice.infra.health;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.scheduler.orderservice.order.client.dto.MemberFeignDto.*;
import static org.apache.hc.core5.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/health-check")
@RequiredArgsConstructor
public class HealthCheckController {

    private final TestService testService;

    @Operation(
            summary = "네트워크 및 어플리케이션 상태 확인"
    )
    @GetMapping
    public ResponseEntity<Void> healthCheck() {
        return new ResponseEntity<>(OK);
    }

    @GetMapping("/test-user")
    public ResponseEntity<StudentResponse> testComponent(
            @RequestHeader(AUTHORIZATION) String accessToken
    ) {
        return new ResponseEntity<>(testService.test(accessToken), OK);
    }
}
