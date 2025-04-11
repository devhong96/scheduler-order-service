package com.scheduler.orderservice.order.client.dto;

import lombok.Getter;
import lombok.Setter;

public class MemberFeignDto {

    @Getter
    @Setter
    public static class AdminResponse {

        private String adminId;

        private String adminName;

        private String username;
    }

    @Getter
    @Setter
    public static class TeacherResponse {

        private String teacherId;

        private String teacherName;

        private String username;
    }

    @Getter
    @Setter
    public static class StudentResponse {

        private String studentId;

        private String username;
    }
}
