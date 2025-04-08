package com.scheduler.orderservice.order.common.dto;

import com.scheduler.orderservice.order.common.domain.Vendor;
import lombok.Getter;
import lombok.Setter;

public class OrderResponseList {

    @Getter
    @Setter
    public static class OrderResponse {
        private Vendor vendor;
        private Object responseData;

        public OrderResponse(Vendor vendor, Object responseData) {
            this.vendor = vendor;
            this.responseData = responseData;
        }
    }
}
