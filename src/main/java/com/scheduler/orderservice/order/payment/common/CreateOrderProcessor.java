package com.scheduler.orderservice.order.payment.common;

import com.scheduler.orderservice.order.common.domain.OrderCategory;
import com.scheduler.orderservice.order.common.domain.OrderType;
import com.scheduler.orderservice.order.common.domain.Vendor;
import com.scheduler.orderservice.order.common.dto.DirectOrderDto;

import static com.scheduler.orderservice.order.client.dto.MemberFeignDto.StudentResponse;

public interface CreateOrderProcessor {

    Boolean supports(Vendor vendor, OrderType orderType);

    void process(String orderId, OrderType orderType, OrderCategory orderCategory, StudentResponse studentResponse,
                 DirectOrderDto directOrderDto, PaymentHistoryDto paymentHistoryDto);
}
