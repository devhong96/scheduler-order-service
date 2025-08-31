package com.scheduler.orderservice.order.payment.nicepay.service;

import com.scheduler.orderservice.infra.exception.custom.PaymentException;
import com.scheduler.orderservice.order.client.MemberServiceClient;
import com.scheduler.orderservice.order.common.component.RedisOrderCache;
import com.scheduler.orderservice.order.common.domain.OrderCategory;
import com.scheduler.orderservice.order.common.domain.OrderType;
import com.scheduler.orderservice.order.common.dto.DirectOrderDto;
import com.scheduler.orderservice.order.payment.common.CreateOrderProcesserFactory;
import com.scheduler.orderservice.order.payment.common.CreateOrderProcessor;
import com.scheduler.orderservice.order.payment.common.PaymentHistoryDto;
import com.scheduler.orderservice.order.payment.nicepay.service.component.CreateNicePayOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.scheduler.orderservice.order.client.dto.MemberFeignDto.StudentResponse;
import static com.scheduler.orderservice.order.common.domain.Vendor.NICEPAY;
import static com.scheduler.orderservice.order.payment.nicepay.dto.NicePayRequest.NicePayPreOrderRequest;
import static com.scheduler.orderservice.order.payment.nicepay.dto.NicePayResponse.NicePayOrderResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class NicePayServiceImpl implements NicePayService {

    private final MemberServiceClient memberServiceClient;
    private final CreateOrderProcesserFactory factory;

    private final CreateNicePayOrder createNicePayOrder;
    private final RedisOrderCache redisOrderCache;

    @Override
    @Transactional
    public NicePayOrderResponse createNicePayOrder(
            String orderId,
            OrderType orderType, OrderCategory orderCategory,
            NicePayPreOrderRequest niceRequest
    ) {
        // 실제 서비스 할 경우 주석을 해제할 것
//        validateNicePayOrder.validateNicePayOrder(niceRequest);

        //바로 결제 장바구니 결제 나눌것
        DirectOrderDto directOrder = redisOrderCache.getDirectOrderInfo(orderId);

        StudentResponse studentResponse = memberServiceClient.getStudentInfo(directOrder.getAccessToken());


        NicePayOrderResponse response = createNicePayOrder.createNicePayOrder(niceRequest)
                .blockOptional().orElseThrow(PaymentException::new);

        CreateOrderProcessor processor = factory.findProcessor(NICEPAY, orderType);

        processor.process(orderType, orderCategory, studentResponse, directOrder, PaymentHistoryDto.fromNicePay(response));


        return response;

    }

}
