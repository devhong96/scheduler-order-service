package com.scheduler.orderservice.order.common.service.component;

import com.scheduler.orderservice.order.common.component.NaverProperties;
import com.scheduler.orderservice.order.common.component.RedisOrderCache;
import com.scheduler.orderservice.order.common.domain.Vendor;
import com.scheduler.orderservice.order.common.dto.DirectOrderDto;
import com.scheduler.orderservice.order.common.dto.OrderCheckoutInfo;
import com.scheduler.orderservice.order.common.event.CreateOrderGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

import static com.scheduler.orderservice.order.common.domain.OrderCategory.PRODUCT;
import static com.scheduler.orderservice.order.common.domain.OrderType.DIRECT;
import static com.scheduler.orderservice.order.common.domain.Vendor.NAVER;
import static com.scheduler.orderservice.order.common.dto.OrderResponseList.OrderResponse;
import static com.scheduler.orderservice.order.payment.naver.dto.NaverPayRequest.NaverCreateOrderRequest;

@Component
@RequiredArgsConstructor
public class NaverCreateOrderService implements CreateOrderGateway {

    private final NaverProperties naverProperties;
    private final RedisOrderCache redisOrderCache;

    @Override
    public Vendor getVendor() {
        return NAVER;
    }

    @Override
    public OrderResponse createOrder(OrderCheckoutInfo info) {

        String vendorReturnUrl = naverProperties.getNaverUrl().getBaseUrl();
        String orderReturnUri = "/order/naver/pay";
        String orderTypePath = info.getOrderType().toString().toLowerCase();
        String orderCategoryIdPath = info.getOrderCategory().toString().toLowerCase();

        if(info.getOrderType().equals(DIRECT)) {
            redisOrderCache.saveDirectOrderInfo(info.getOrderId(), new DirectOrderDto(info.getAccessToken(), info.getProductId(), info.getTotalQuantity()));
        }

        String returnUrl = Path.of(vendorReturnUrl, orderReturnUri, orderTypePath, orderCategoryIdPath).toString();

        NaverCreateOrderRequest naverCreateOrderRequest = NaverCreateOrderRequest.builder()
                .merchantPayKey(info.getOrderId())
                .productName(info.getProductName())
                .productCount(info.getTotalQuantity())
                .totalPayAmount(info.getAmountSum())
                .taxScopeAmount(info.getVatAmount())
                .taxExScopeAmount(0)
                .returnUrl(returnUrl)
                .build();

        if(info.getOrderCategory().equals(PRODUCT)) {


        }

        return new OrderResponse(NAVER, naverCreateOrderRequest);

    }
}
