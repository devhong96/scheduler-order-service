package com.scheduler.orderservice.infra.health;

import com.scheduler.orderservice.order.client.MemberServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.scheduler.orderservice.order.client.dto.MemberFeignDto.StudentResponse;

@Service
@RequiredArgsConstructor
public class TestService {

    private final MemberServiceClient memberServiceClient;

    @Transactional
    public StudentResponse test (
            String accessToken
    ) {
        return memberServiceClient.getStudentInfo(accessToken);
    }
}
