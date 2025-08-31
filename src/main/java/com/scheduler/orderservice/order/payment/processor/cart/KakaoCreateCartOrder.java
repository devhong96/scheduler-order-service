package com.scheduler.orderservice.order.payment.processor.cart;

import com.scheduler.orderservice.cart.dto.CartDto;
import com.scheduler.orderservice.cart.repository.CartJpaRepository;
import com.scheduler.orderservice.cart.repository.CartRepository;
import com.scheduler.orderservice.order.common.domain.OrderCategory;
import com.scheduler.orderservice.order.common.domain.OrderType;
import com.scheduler.orderservice.order.common.domain.Orders;
import com.scheduler.orderservice.order.common.domain.Vendor;
import com.scheduler.orderservice.order.common.dto.DirectOrderDto;
import com.scheduler.orderservice.order.common.repository.OrdersJpaRepository;
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

    @Override
    public Boolean supports(Vendor vendor, OrderType orderType) {
        return vendor == KAKAO && orderType == CART;
    }

    @Override
    @Transactional
    public void process(OrderType orderType, OrderCategory orderCategory, StudentResponse studentResponse,
                        DirectOrderDto directOrderDto, PaymentHistoryDto paymentHistoryDto
    ) {
        List<CartDto> cartDtoByStudentId = cartRepository.getCartDtoByStudentId(studentResponse.getStudentId(), true);

        List<Orders> ordersList = new ArrayList<>();

        for (CartDto cartDto : cartDtoByStudentId) {
            ordersList.add(Orders.create(KAKAO, CART, orderCategory, studentResponse,
                    cartDto.getProductId(), cartDto.getProductName(), cartDto.getQuantity(),
                    paymentHistoryDto)
            );
        }

        ordersJpaRepository.saveAll(ordersList);
        cartJpaRepository.deleteByStudentIdAndChecked(studentResponse.getStudentId(), true);

    }
}
