package com.scheduler.orderservice.order.client.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

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
    public static class AuthorResponse {

        private String authorId;

        private String authorName;

        private String username;
    }

    @Getter
    @Setter
    public static class StudentResponse {

        private String studentId;

        private String username;
    }

    @Getter
    @Setter
    public static class FeignOwnedEbookDto {

        private String ebookId;
        private String orderId;
        private String ebookCoverUrl;
        private String ebookName;
        private String authorName;

        public FeignOwnedEbookDto(String ebookId, String orderId,
                                  String ebookCoverUrl, String ebookName, String authorName) {
            this.ebookId = ebookId;
            this.orderId = orderId;
            this.ebookCoverUrl = ebookCoverUrl;
            this.ebookName = ebookName;
            this.authorName = authorName;
        }
    }

    @Getter
    @Setter
    public static class CreateOwnedEbookListDto {

        private String readerId;

        private List<FeignOwnedEbookDto> ownedEbookResponseList;

        public CreateOwnedEbookListDto(String readerId, List<FeignOwnedEbookDto> ownedEbookResponseList) {
            this.readerId = readerId;
            this.ownedEbookResponseList = ownedEbookResponseList;
        }
    }
}
