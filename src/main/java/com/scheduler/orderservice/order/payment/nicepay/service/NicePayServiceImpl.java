package com.scheduler.orderservice.order.payment.nicepay.service;

import com.scheduler.orderservice.infra.exception.custom.PaymentException;
import com.scheduler.orderservice.order.client.MemberServiceClient;
import com.scheduler.orderservice.order.common.component.RedisOrderCache;
import com.scheduler.orderservice.order.common.domain.OrderCategory;
import com.scheduler.orderservice.order.common.domain.OrderType;
import com.scheduler.orderservice.order.common.dto.DirectOrderDto;
import com.scheduler.orderservice.order.payment.nicepay.service.component.CancelNicePayOrder;
import com.scheduler.orderservice.order.payment.nicepay.service.component.CreateNicePayOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.scheduler.orderservice.order.client.dto.MemberFeignDto.StudentResponse;
import static com.scheduler.orderservice.order.payment.nicepay.dto.NicePayRequest.NicePayCancelOrderRequest;
import static com.scheduler.orderservice.order.payment.nicepay.dto.NicePayRequest.NicePayPreOrderRequest;
import static com.scheduler.orderservice.order.payment.nicepay.dto.NicePayResponse.NicePayCancelOrderResponse;
import static com.scheduler.orderservice.order.payment.nicepay.dto.NicePayResponse.NicePayOrderResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class NicePayServiceImpl implements NicePayService {

    private final MemberServiceClient memberServiceClient;

    private final CreateNicePayOrder createNicePayOrder;
    private final CancelNicePayOrder cancelNicePayOrder;
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

        DirectOrderDto directOrder = redisOrderCache.getDirectOrderInfo(orderId);

        StudentResponse studentInfo = memberServiceClient.getStudentInfo(directOrder.getAccessToken());


        String studentId = studentInfo.getStudentId();
        String username = studentInfo.getUsername();
        Integer quantity = directOrder.getQuantity();

        NicePayOrderResponse response = createNicePayOrder.createNicePayOrder(niceRequest)
                .blockOptional().orElseThrow(PaymentException::new);

//        applyOrder(orderType, studentId, username, quantity, orderCategory, response);

        return response;

    }

    //TODO 추가 로직
    @Override
    public NicePayCancelOrderResponse cancelNicepayOrder(NicePayCancelOrderRequest nicePayCancelOrderRequest) {

        return cancelNicePayOrder.cancelNicepayOrder(nicePayCancelOrderRequest)
                .blockOptional().orElseThrow(PaymentException::new);
    }

}
