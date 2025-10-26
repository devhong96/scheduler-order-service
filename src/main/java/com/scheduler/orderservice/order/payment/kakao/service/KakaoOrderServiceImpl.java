package com.scheduler.orderservice.order.payment.kakao.service;

import com.scheduler.orderservice.infra.exception.custom.PaymentException;
import com.scheduler.orderservice.order.client.MemberServiceClient;
import com.scheduler.orderservice.order.common.component.RedisOrderCache;
import com.scheduler.orderservice.order.common.domain.OrderCategory;
import com.scheduler.orderservice.order.common.domain.OrderType;
import com.scheduler.orderservice.order.common.dto.DirectOrderDto;
import com.scheduler.orderservice.order.common.dto.KakaoDto;
import com.scheduler.orderservice.order.payment.common.CreateOrderProcesserFactory;
import com.scheduler.orderservice.order.payment.common.CreateOrderProcessor;
import com.scheduler.orderservice.order.payment.common.PaymentHistoryDto;
import com.scheduler.orderservice.order.payment.kakao.service.component.ApproveKakaoOrder;
import com.scheduler.orderservice.order.payment.kakao.service.component.GetKakaoOrder;
import com.scheduler.orderservice.order.payment.kakao.service.component.KakaoPreOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.scheduler.orderservice.order.client.dto.MemberFeignDto.StudentResponse;
import static com.scheduler.orderservice.order.common.domain.OrderType.DIRECT;
import static com.scheduler.orderservice.order.common.domain.Vendor.KAKAO;
import static com.scheduler.orderservice.order.payment.kakao.dto.KakaoPayRequest.KakaoPreOrderRequest;
import static com.scheduler.orderservice.order.payment.kakao.dto.KakaoPayResponse.KakaoApproveOrderResponse;
import static com.scheduler.orderservice.order.payment.kakao.dto.KakaoPayResponse.KakaoPreOrderResponse;
import static com.scheduler.orderservice.order.payment.kakao.dto.KakaoSearchOrderDto.KakaoSearchOrderResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoOrderServiceImpl implements KakaoOrderService {

    private final MemberServiceClient memberServiceClient;
    private final GetKakaoOrder getKakaoOrder;
    private final ApproveKakaoOrder approveKakaoOrder;
    private final KakaoPreOrder kakaoPreOrder;

    private final RedisOrderCache redisOrderCache;
    private final CreateOrderProcesserFactory factory;

    @Override
    public KakaoPreOrderResponse kakaoPreOrder(
            String accessToken, KakaoPreOrderRequest kakaoPreOrderRequest
    ) {

        String studentId = memberServiceClient.getStudentInfo(accessToken).getStudentId();

        String itemCode = kakaoPreOrderRequest.getItemCode();

        String productName = kakaoPreOrderRequest.getItemName();

        Integer quantity = kakaoPreOrderRequest.getQuantity();

        String orderId = kakaoPreOrderRequest.getPartnerOrderId();

        OrderType orderType = kakaoPreOrderRequest.getOrderType();

        if(orderType.equals(DIRECT)) {
            redisOrderCache.saveDirectOrderInfo(orderId, new DirectOrderDto(accessToken, productName, itemCode, quantity));
        }

        KakaoPreOrderResponse response = kakaoPreOrder.kakaoPreOrderResponse(orderId, kakaoPreOrderRequest)
                .blockOptional().orElseThrow(PaymentException::new);

        String tid = response.getTid();
        redisOrderCache.saveKakaoOrderInfo(orderId, new KakaoDto(accessToken, tid, studentId, System.currentTimeMillis()));

        return response;
    }

    @Override
    public KakaoApproveOrderResponse createKakaoOrder(
            OrderType orderType, OrderCategory orderCategory,
            String orderId, String pgToken
    ) {

        //바로 결제 장바구니 결제 나눌것
        DirectOrderDto directOrderDto = redisOrderCache.getDirectOrderInfo(orderId);
        StudentResponse studentResponse = memberServiceClient.getStudentInfo(directOrderDto.getAccessToken());

        KakaoApproveOrderResponse response = approveKakaoOrder.kakaoApproveOrderResponse(orderId, pgToken)
                .blockOptional().orElseThrow(PaymentException::new);

        CreateOrderProcessor processor = factory.findProcessor(KAKAO, orderType);

        processor.process(orderId, orderType, orderCategory, studentResponse, directOrderDto, PaymentHistoryDto.fromKakao(response));

        return response;
    }

    //카카오 공통 로직
    @Override
    public KakaoSearchOrderResponse searchKakaoOrder(String tid) {

        return getKakaoOrder.getKakaoOrderResponse(tid)
                .blockOptional().orElseThrow(PaymentException::new);
    }
}
