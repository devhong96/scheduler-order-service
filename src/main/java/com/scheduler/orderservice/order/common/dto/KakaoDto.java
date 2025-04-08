package com.scheduler.orderservice.order.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoDto {

    private String accessToken;
    private String tid;
    private String studentId;
    private long createdAt;

    public KakaoDto(String accessToken, String tid, String studentId, long createdAt) {
        this.accessToken = accessToken;
        this.tid = tid;
        this.studentId = studentId;
        this.createdAt = createdAt;
    }
}
