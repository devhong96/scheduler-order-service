package com.scheduler.orderservice.order.common.dto;

import com.scheduler.orderservice.order.common.domain.OrderCategory;
import com.scheduler.orderservice.order.common.domain.OrderType;
import com.scheduler.orderservice.order.common.domain.Vendor;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderCheckoutInfo {

    private final String accessToken;

    private final Vendor vendor;
    private final OrderCategory orderCategory;
    private final OrderType orderType;

    private final String orderId;
    private final String productId;
    private final String productCoverUrl;
    private final String productName;
    private final Integer totalQuantity;
    private final Integer eachCount;
    private final Integer amountSum;
    private final Integer vatAmount;
}
