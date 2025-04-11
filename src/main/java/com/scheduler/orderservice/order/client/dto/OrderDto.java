package com.scheduler.orderservice.order.client.dto;

import com.scheduler.orderservice.order.common.domain.OrderCategory;
import com.scheduler.orderservice.order.common.domain.Vendor;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static com.scheduler.orderservice.order.payment.kakao.dto.KakaoPayRequest.KakaoOrderResponse;
import static com.scheduler.orderservice.order.payment.naver.dto.NaverPayResponse.NaverOrderResponse.Detail;
import static com.scheduler.orderservice.order.payment.nicepay.dto.NicePayResponse.NicePayOrderResponse;
import static jakarta.persistence.EnumType.STRING;

public class OrderDto {

    @Getter
    @Setter
    public static class CartOrderListDto {
        private String username;
        private List<EbookCartOrderRequest> ebookOrderInfoList;

        public CartOrderListDto(String username, List<EbookCartOrderRequest> ebookOrderInfoList) {
            this.username = username;
            this.ebookOrderInfoList = ebookOrderInfoList;
        }
    }

    @Getter
    @Setter
    public static class EbookCartOrderRequest {

        private String ebookOrderId;

        private String vendorTid;

        @Enumerated(STRING)
        private OrderCategory orderCategory;

        private SingleEbookCartResponse singleEbookCartResponse;

        private String paymentMethod;

        private Vendor vendor;

        private String cardName;

    }

    @Getter
    @Setter
    public static class SingleEbookCartResponse {

        private String cartId;

        private String studentId;

        private String ebookId;

        private String ebookName;

        private String ebookCoverUrl;

        private String authorName;

        private Integer price;

        private Integer quantity;

        private Integer donationAmount;

        private Boolean isSelected;
    }


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateKakaoDirectOrderDto {

        private String studentId;

        private String username;

        private Integer quantity;

        @Enumerated(STRING)
        private OrderCategory orderCategory;

        private KakaoOrderResponse response;
    }


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateNaverDirectOrderDto {

        private String studentId;

        private String username;

        private Integer quantity;

        @Enumerated(STRING)
        private OrderCategory orderCategory;

        private Detail detail;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateNicePayDirectOrderDto {

        private String studentId;

        private String username;

        private Integer quantity;

        @Enumerated(STRING)
        private OrderCategory orderCategory;

        private NicePayOrderResponse response;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CancelOrderInfoResponse {
        @Enumerated(STRING)
        private Vendor vendor;
        private String vendorTid;
        private String ebookId;
        private String ebookName;
        private Integer cancelAmount;
    }
}
