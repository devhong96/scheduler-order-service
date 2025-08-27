package com.scheduler.orderservice.order.common.service.component;

import com.scheduler.orderservice.order.common.component.NicePayProperties;
import com.scheduler.orderservice.order.common.component.RedisOrderCache;
import com.scheduler.orderservice.order.common.domain.Vendor;
import com.scheduler.orderservice.order.common.dto.DirectOrderDto;
import com.scheduler.orderservice.order.common.dto.OrderCheckoutInfo;
import com.scheduler.orderservice.order.common.event.CreateOrderGateway;
import com.scheduler.orderservice.order.payment.nicepay.dto.NicePayResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

import static com.scheduler.orderservice.order.common.domain.OrderCategory.PRODUCT;
import static com.scheduler.orderservice.order.common.domain.OrderType.DIRECT;
import static com.scheduler.orderservice.order.common.domain.Vendor.NICEPAY;
import static com.scheduler.orderservice.order.common.dto.OrderResponseList.OrderResponse;

@Component
@RequiredArgsConstructor
public class NICEPayCreateOrderService implements CreateOrderGateway {

    private final NicePayProperties nicePayProperties;
    private final RedisOrderCache redisOrderCache;

    @Override
    public Vendor getVendor() {
        return NICEPAY;
    }

    @Override
    public OrderResponse createOrder(OrderCheckoutInfo info) {
        String vendorReturnUrl = nicePayProperties.getNiceUrl().getBaseUrl();
        String orderReturnUri = "order/nicepay/pay";
        String orderTypePath = info.getOrderType().toString().toLowerCase();
        String orderCategoryIdPath = info.getOrderCategory().toString().toLowerCase();

        if(info.getOrderType().equals(DIRECT)) {
            redisOrderCache.saveDirectOrderInfo(info.getOrderId(), new DirectOrderDto(info.getAccessToken(), info.getProductId(), info.getTotalQuantity()));
        }

        String returnUrl = Path.of(vendorReturnUrl, orderReturnUri, orderTypePath, orderCategoryIdPath).toString();

        NicePayResponse.NicePayPreOrderResponse nicePayPreOrderResponse = NicePayResponse.NicePayPreOrderResponse.builder()
                .orderId(info.getOrderId())
                .amount(info.getAmountSum())
                .goodsName(info.getProductName())
                .returnUrl(returnUrl)
                .build();

        if(info.getOrderCategory().equals(PRODUCT)) {

        }

        return new OrderResponse(NICEPAY, nicePayPreOrderResponse);
    }
}
