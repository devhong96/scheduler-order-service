package com.scheduler.orderservice.order.payment.naver.service;

import com.scheduler.orderservice.infra.exception.custom.PaymentException;
import com.scheduler.orderservice.order.client.MemberServiceClient;
import com.scheduler.orderservice.order.common.component.RedisOrderCache;
import com.scheduler.orderservice.order.common.domain.OrderCategory;
import com.scheduler.orderservice.order.common.domain.OrderType;
import com.scheduler.orderservice.order.common.dto.DirectOrderDto;
import com.scheduler.orderservice.order.payment.event.direct.vendor.NaverAfterDirectOrderEvent;
import com.scheduler.orderservice.order.payment.event.direct.vendor.NaverDirectOrderEvent;
import com.scheduler.orderservice.order.payment.naver.service.component.CancelNaverOrder;
import com.scheduler.orderservice.order.payment.naver.service.component.CreateNaverOrder;
import com.scheduler.orderservice.order.payment.naver.service.component.SearchNaverOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import static com.scheduler.orderservice.order.client.dto.MemberFeignDto.StudentResponse;
import static com.scheduler.orderservice.order.payment.naver.dto.NaverPayRequest.SearchNaverOrderHistoryDto;
import static com.scheduler.orderservice.order.payment.naver.dto.NaverPayResponse.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverOrderServiceImpl implements NaverOrderService {

    private final MemberServiceClient memberServiceClient;
    private final RedisOrderCache redisOrderCache;

    private final CreateNaverOrder createNaverOrder;
    private final CancelNaverOrder cancelNaverOrder;
    private final SearchNaverOrder searchNaverOrder;

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public NaverOrderResponse createNaverOrder(
            OrderType orderType, OrderCategory orderCategory,
            String orderId,
            String resultCode, String paymentId
    ) {
        DirectOrderDto directOrder = redisOrderCache.getDirectOrderInfo(orderId);

        StudentResponse studentInfo = memberServiceClient.getStudentInfo(directOrder.getAccessToken());

        String studentId = studentInfo.getStudentId();
        String username = studentInfo.getUsername();
        Integer quantity = directOrder.getQuantity();

        NaverOrderResponse response = createNaverOrder.createNaverOrderResponse(resultCode, paymentId)
                .blockOptional().orElseThrow(PaymentException::new);

        applyOrder(orderType, studentId, username, quantity, orderCategory, response);

        return response;
    }

    public void applyOrder(OrderType orderType, String studentId, String username, Integer quantity,
                            OrderCategory orderCategory, NaverOrderResponse response
    ) {
        switch (orderType) {
            case DIRECT: {
                eventPublisher.publishEvent(new NaverDirectOrderEvent(this, studentId, username, quantity, orderCategory, response));
                eventPublisher.publishEvent(new NaverAfterDirectOrderEvent(this, studentId, username, quantity, orderCategory, response));
                break;
            }

            case CART: {

                break;
            }
        }
    }

    //TODO 후속 처리
    @Override
    public NaverCancelOrderResponse cancelNaverOrder(
            CancelNaverOrderDto cancelNaverOrderDto
    ) {
        NaverCancelOrderResponse response = cancelNaverOrder.cancelNaverOrderResponse(cancelNaverOrderDto)
                .blockOptional().orElseThrow(PaymentException::new);

        eventPublisher.publishEvent(response);

        return response;
    }

    @Override
    public SearchNaverOrderResponse searchNaverOrder(
            String paymentId, SearchNaverOrderHistoryDto searchHistory
    ) {
        return searchNaverOrder.searchNaverOrder(paymentId, searchHistory)
                .blockOptional().orElseThrow(PaymentException::new);
    }

}
