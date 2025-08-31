package com.scheduler.orderservice.order.payment.naver.service;

import com.scheduler.orderservice.infra.exception.custom.PaymentException;
import com.scheduler.orderservice.order.client.MemberServiceClient;
import com.scheduler.orderservice.order.common.component.RedisOrderCache;
import com.scheduler.orderservice.order.common.domain.OrderCategory;
import com.scheduler.orderservice.order.common.domain.OrderType;
import com.scheduler.orderservice.order.common.dto.DirectOrderDto;
import com.scheduler.orderservice.order.payment.common.CreateOrderProcesserFactory;
import com.scheduler.orderservice.order.payment.common.CreateOrderProcessor;
import com.scheduler.orderservice.order.payment.common.PaymentHistoryDto;
import com.scheduler.orderservice.order.payment.naver.service.component.CreateNaverOrder;
import com.scheduler.orderservice.order.payment.naver.service.component.SearchNaverOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.scheduler.orderservice.order.client.dto.MemberFeignDto.StudentResponse;
import static com.scheduler.orderservice.order.common.domain.Vendor.NAVER;
import static com.scheduler.orderservice.order.payment.naver.dto.NaverPayRequest.SearchNaverOrderHistoryDto;
import static com.scheduler.orderservice.order.payment.naver.dto.NaverPayResponse.NaverOrderResponse;
import static com.scheduler.orderservice.order.payment.naver.dto.NaverPayResponse.SearchNaverOrderResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverOrderServiceImpl implements NaverOrderService {

    private final MemberServiceClient memberServiceClient;
    private final RedisOrderCache redisOrderCache;
    private final CreateOrderProcesserFactory factory;

    private final CreateNaverOrder createNaverOrder;
    private final SearchNaverOrder searchNaverOrder;


    @Override
    public NaverOrderResponse createNaverOrder(
            OrderType orderType, OrderCategory orderCategory,
            String orderId,
            String resultCode, String paymentId
    ) {
        // 장바구니 결제와 즉시 결제 여기서 부터 나눠야 할듯.
        DirectOrderDto directOrder = redisOrderCache.getDirectOrderInfo(orderId);

        StudentResponse studentResponse = memberServiceClient.getStudentInfo(directOrder.getAccessToken());

        NaverOrderResponse response = createNaverOrder.createNaverOrderResponse(resultCode, paymentId)
                .blockOptional().orElseThrow(PaymentException::new);

        CreateOrderProcessor processor = factory.findProcessor(NAVER, orderType);

        processor.process(orderType, orderCategory, studentResponse, directOrder,
                PaymentHistoryDto.fromDetail(response.getBody().getDetail())
        );

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
