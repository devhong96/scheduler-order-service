package com.scheduler.orderservice.order.common.service.component;

import com.scheduler.orderservice.order.common.domain.OrderCategory;
import com.scheduler.orderservice.order.common.domain.OrderType;
import com.scheduler.orderservice.order.common.domain.Vendor;
import com.scheduler.orderservice.order.common.dto.OrderCheckoutInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.scheduler.orderservice.order.common.dto.OrderRequest.PreOrderRequest;
import static com.scheduler.orderservice.order.common.dto.OrderRequest.ProductItems;

@Component
@RequiredArgsConstructor
public class OrderCalculateFactory {

    public OrderCheckoutInfo createOrderCalculator(
            String accessToken, OrderType orderType, OrderCategory orderCategory, Vendor vendor,
            PreOrderRequest preOrderRequest
    ) {
        List<ProductItems> productItems = preOrderRequest.getProductItems();

        int totalQuantity = productItems.stream()
                .mapToInt(ProductItems::getCount).sum();

        String orderId = "O" + LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));

        String productId = productItems.get(0).getUid();

        String productCoverUrl = "";

        String productName = preOrderRequest.getProductName();

        int eachCount = preOrderRequest.getQuantity();

        //
        int amountSum = preOrderRequest.getPrice();

        double supplyPriceDouble = amountSum / 1.1;

        int supplyPrice = (int) Math.round(supplyPriceDouble);

        Integer vatAmount = amountSum - supplyPrice;

        return new OrderCheckoutInfo(accessToken, vendor, orderCategory, orderType, orderId, productId, productCoverUrl, productName, totalQuantity, eachCount, amountSum, vatAmount);

    }
}
