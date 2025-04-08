package com.scheduler.orderservice.order.payment.naver.dto;

import com.scheduler.orderservice.order.payment.naver.domain.CategoryId;
import com.scheduler.orderservice.order.payment.naver.domain.CategoryType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static jakarta.persistence.EnumType.STRING;

public class NaverPayRequest {

    @Getter
    @Builder
    public static class NaverCreateOrderRequest {

        private String clientId;

        private String chainId;

        //가맹점 결제 번호
        private String merchantPayKey;

        //대표 상품 명
        private String productName;

        //전체 합
        private Integer productCount;

        private Integer totalPayAmount;

        private Integer taxScopeAmount;

        private Integer taxExScopeAmount;

        private String returnUrl;

        private List<ProductItems> productItems;
    }

    @Getter
    @Setter
    public static class ProductItems {

        @Enumerated(STRING)
        private CategoryType categoryType;

        @Enumerated(STRING)
        private CategoryId categoryId;

        //bookId
        private String uid;

        private String name;

        private String payReferrer;

        private Integer count;
    }

    @Getter
    @Setter
    public static class SearchNaverOrderHistoryDto {
        private String pageNumber;
        private String rowsPerPage;
    }
}
