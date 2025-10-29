package com.scheduler.orderservice.order.payment.processor.cart;

import com.scheduler.orderservice.cart.dto.CartDto;
import com.scheduler.orderservice.cart.repository.CartJpaRepository;
import com.scheduler.orderservice.cart.repository.CartRepository;
import com.scheduler.orderservice.order.common.domain.*;
import com.scheduler.orderservice.order.common.dto.DirectOrderDto;
import com.scheduler.orderservice.order.common.repository.OrderItemJpaRepository;
import com.scheduler.orderservice.order.common.repository.OrdersJpaRepository;
import com.scheduler.orderservice.order.common.repository.PaymentJpaRepository;
import com.scheduler.orderservice.order.payment.common.CreateOrderProcessor;
import com.scheduler.orderservice.order.payment.common.PaymentHistoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.scheduler.orderservice.order.client.dto.MemberFeignDto.StudentResponse;
import static com.scheduler.orderservice.order.common.domain.OrderType.CART;
import static com.scheduler.orderservice.order.common.domain.Vendor.KAKAO;

@Service
@RequiredArgsConstructor
public class KakaoCreateCartOrder implements CreateOrderProcessor {

    private final CartRepository cartRepository;
    private final CartJpaRepository cartJpaRepository;
    private final OrdersJpaRepository ordersJpaRepository;
    private final OrderItemJpaRepository orderItemJpaRepository;
    private final PaymentJpaRepository paymentJpaRepository;

    @Override
    public Boolean supports(Vendor vendor, OrderType orderType) {
        return vendor == KAKAO && orderType == CART;
    }

    @Override
    @Transactional
    public void process(String orderId, OrderType orderType, OrderCategory orderCategory, StudentResponse studentResponse,
                        DirectOrderDto directOrderDto, PaymentHistoryDto paymentHistoryDto
    ) {
        List<CartDto> cartDtoByStudentId = cartRepository.getCartDtoByStudentId(studentResponse.getStudentId(), true);

        Orders orders = Orders.create(orderId, KAKAO, paymentHistoryDto.getTid(), studentResponse);
        paymentJpaRepository.save(PaymentInfo.create(paymentHistoryDto));

        List<OrderItems> orderItemsList = new ArrayList<>();

        for (CartDto cartDto : cartDtoByStudentId) {
            orderItemsList.add(OrderItems.create(orderId, CART, orderCategory, cartDto.getProductId(), cartDto.getProductName(), cartDto.getQuantity()));
        }

        orderItemJpaRepository.saveAll(orderItemsList);
        ordersJpaRepository.save(orders);

        cartJpaRepository.deleteByStudentIdAndChecked(studentResponse.getStudentId(), true);

    }
}
