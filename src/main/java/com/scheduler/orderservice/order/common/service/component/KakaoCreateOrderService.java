package com.scheduler.orderservice.order.common.service.component;

import com.scheduler.orderservice.order.common.domain.Vendor;
import com.scheduler.orderservice.order.common.dto.OrderCheckoutInfo;
import com.scheduler.orderservice.order.common.event.CreateOrderGateway;
import com.scheduler.orderservice.order.payment.kakao.dto.KakaoPayRequest.KakaoPreOrderRequest;
import com.scheduler.orderservice.order.payment.kakao.service.KakaoOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.scheduler.orderservice.order.common.domain.Vendor.KAKAO;
import static com.scheduler.orderservice.order.common.dto.OrderResponseList.OrderResponse;
import static com.scheduler.orderservice.order.payment.kakao.dto.KakaoPayResponse.KakaoPreOrderResponse;

@Component
@RequiredArgsConstructor
public class KakaoCreateOrderService implements CreateOrderGateway {

    private final KakaoOrderService kakaoOrderService;

    @Override
    public Vendor getVendor() {
        return KAKAO;
    }

    @Override
    public OrderResponse createOrder(OrderCheckoutInfo info) {

        String itemName = info.getProductName();

        if (info.getTotalQuantity() > 1) {
            itemName = info.getProductName() + "외" + (info.getTotalQuantity() - 1) + "개";
        }

        KakaoPreOrderRequest kakaoPreOrderRequest = KakaoPreOrderRequest.builder()
                .partnerOrderId(info.getOrderId())
                .itemCode(info.getProductId())
                .itemName(itemName)
                .productCoverUrl(info.getProductCoverUrl())
                .quantity(info.getTotalQuantity())
                .totalAmount(info.getAmountSum())
                .vatAmount(info.getVatAmount())
                .taxFreeAmount(0)
                .orderType(info.getOrderType())
                .orderCategory(info.getOrderCategory())
                .build();

        KakaoPreOrderResponse kakaoPreOrder = kakaoOrderService.kakaoPreOrder(info.getAccessToken(), kakaoPreOrderRequest);

        return new OrderResponse(KAKAO, kakaoPreOrder);
    }
}
