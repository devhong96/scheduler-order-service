package com.scheduler.orderservice.order.client.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class EbookDto {

    @Getter
    @Setter
    public static class FeignEbookResponse {
        private String ebookId;
        private String coverFileUrl;
        private String ebookName;
        private String authorName;
        private Integer price;
    }

    @Getter
    @Setter
    public static class ExistsEbookDto {

        private List<String> ebookList = new ArrayList<>();

    }
}
