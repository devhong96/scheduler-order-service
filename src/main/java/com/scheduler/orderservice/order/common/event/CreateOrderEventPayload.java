package com.scheduler.orderservice.order.common.event;

import com.scheduler.orderservice.order.common.domain.OrderCategory;
import com.scheduler.orderservice.order.common.domain.OrderType;
import com.scheduler.orderservice.order.common.domain.Vendor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.scheduler.orderservice.order.common.dto.OrderRequest.PreOrderRequest;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderEventPayload {

    private OrderType orderType;
    private OrderCategory orderCategory;
    private Vendor vendor;
    private PreOrderRequest preOrderRequest;
}
