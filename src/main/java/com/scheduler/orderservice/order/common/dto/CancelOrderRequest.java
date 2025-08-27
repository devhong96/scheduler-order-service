package com.scheduler.orderservice.order.common.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CancelOrderRequest {

    private String refundReason;

        private List<SingleCancelOrder> singleCancelOrders = new ArrayList<>();

        @Getter
        @Setter
        public static class SingleCancelOrder {
            private String orderId;
            private String productId;
            private Integer cancelAmount;
        }
}
