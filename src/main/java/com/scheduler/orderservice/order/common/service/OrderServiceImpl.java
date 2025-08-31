package com.scheduler.orderservice.order.common.service;

import com.scheduler.orderservice.order.client.MemberServiceClient;
import com.scheduler.orderservice.order.common.domain.OrderCategory;
import com.scheduler.orderservice.order.common.domain.OrderType;
import com.scheduler.orderservice.order.common.domain.Vendor;
import com.scheduler.orderservice.order.common.dto.CancelOrderRequest;
import com.scheduler.orderservice.order.common.dto.OrderCheckoutInfo;
import com.scheduler.orderservice.order.common.event.*;
import com.scheduler.orderservice.order.common.service.component.OrderCalculateFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.scheduler.orderservice.order.client.dto.MemberFeignDto.StudentResponse;
import static com.scheduler.orderservice.order.common.dto.OrderRequest.PreOrderRequest;
import static com.scheduler.orderservice.order.common.dto.OrderResponseList.OrderResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderCalculateFactory orderCalculateFactory;

    private final CreatePaymentGatewayFactory paymentGatewayFactory;
    private final CancelPaymentGatewayFactory cancelPaymentGatewayFactory;

    private final OrderTransactionService orderTransactionService;
    private final MemberServiceClient memberServiceClient;


    @Override
    @Transactional
    public OrderResponse createOrder(
            String accessToken,
            OrderType orderType, OrderCategory orderCategory, Vendor vendor,
            PreOrderRequest preOrderRequest
    ) {

        OrderCheckoutInfo orderCheckoutInfo = orderCalculateFactory.createOrderCalculator(accessToken, orderType, orderCategory, vendor, preOrderRequest);

        CreateOrderGateway createOrderGateway = paymentGatewayFactory.getVendor(vendor);

        return createOrderGateway.createOrder(orderCheckoutInfo);
    }

    @Override
    public void cancelOrder(
            String accessToken, String orderId, CancelOrderRequest cancelOrderRequest
    ) {
        StudentResponse studentInfo = memberServiceClient.getStudentInfo(accessToken);

        CancelOrderPayload cancelPayload = orderTransactionService
                .cancelOrderEvent(orderId, studentInfo, cancelOrderRequest);

        CancelOrderGateway cancelOrderGateway = cancelPaymentGatewayFactory.getVendor(cancelPayload.getVendor());

        cancelOrderGateway.refund(cancelPayload);

    }
}
