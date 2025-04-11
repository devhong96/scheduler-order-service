package com.scheduler.orderservice.order.common.service;

import com.scheduler.orderservice.order.common.component.NaverProperties;
import com.scheduler.orderservice.order.common.component.NicePayProperties;
import com.scheduler.orderservice.order.common.component.RedisOrderCache;
import com.scheduler.orderservice.order.common.domain.OrderCategory;
import com.scheduler.orderservice.order.common.domain.OrderType;
import com.scheduler.orderservice.order.common.domain.Vendor;
import com.scheduler.orderservice.order.common.dto.DirectOrderDto;
import com.scheduler.orderservice.order.payment.kakao.service.KakaoOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.scheduler.orderservice.order.common.domain.OrderCategory.PRODUCT;
import static com.scheduler.orderservice.order.common.domain.OrderType.DIRECT;
import static com.scheduler.orderservice.order.common.domain.Vendor.*;
import static com.scheduler.orderservice.order.common.dto.OrderDto.*;
import static com.scheduler.orderservice.order.common.dto.OrderResponseList.OrderResponse;
import static com.scheduler.orderservice.order.payment.kakao.dto.KakaoPayRequest.KakaoPreOrderResponse;
import static com.scheduler.orderservice.order.payment.kakao.dto.KakaoPayRequest.KakaoPreOrderRequest;
import static com.scheduler.orderservice.order.payment.naver.dto.NaverPayRequest.NaverCreateOrderRequest;
import static com.scheduler.orderservice.order.payment.nicepay.dto.NicePayResponse.NicePayPreOrderResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements com.scheduler.orderservice.order.common.service.OrderService {

    private final NaverProperties naverProperties;
    private final NicePayProperties nicePayProperties;
    private final KakaoOrderService kakaoOrderService;
    private final RedisOrderCache redisOrderCache;

    @Override
    @Transactional
    public OrderResponse createOrder(
            String accessToken,
            OrderType orderType, Vendor vendor, OrderCategory orderCategory,
            OrderRequest orderRequest
    ) {

        List<ProductItems> productItems = orderRequest.getProductItems();

        List<String> ebookIds = productItems.stream()
                .map(ProductItems::getUid)
                .toList();

        int totalQuantity = orderRequest.getQuantity();

        String orderId = "O" + LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));

        String ebookId = productItems.get(0).getUid();

        String ebookCoverUrl = "";

        String ebookName = orderRequest.getProductName();

        int eachCount = ebookIds.size();

        //
        int amountSum = orderRequest.getPrice();

        int donationAmountSum = productItems.stream()
                .mapToInt(ProductItems::getDonationAmount)
                .sum();

        int vatRate = 10;

        int vatAmount = amountSum - (amountSum * 100) / (100 + vatRate);

        switch (vendor) {

            case KAKAO -> {

                String itemName = ebookName;

                if (eachCount > 1) {
                    itemName = ebookName + "외" + (eachCount - 1) + "개";
                }

                KakaoPreOrderRequest kakaoPreOrderRequest = KakaoPreOrderRequest.builder()
                        .partnerOrderId(orderId)
                        .itemCode(ebookId)
                        .itemName(itemName)
                        .ebookCoverUrl(ebookCoverUrl)
                        .quantity(totalQuantity)
                        .totalAmount(amountSum)
                        .vatAmount(vatAmount)
                        .taxFreeAmount(0)
                        .donationAmount(donationAmountSum)
                        .orderType(orderType)
                        .orderCategory(orderCategory)
                        .build();

                KakaoPreOrderResponse kakaoEbookPreOrder = kakaoOrderService.kakaoPreOrder(accessToken, kakaoPreOrderRequest);

                if(orderCategory.equals(PRODUCT)) {

                }

                return new OrderResponse(KAKAO, kakaoEbookPreOrder);
            }

            case NAVER -> {
                String vendorReturnUrl = naverProperties.getNaverUrl().getBaseUrl();
                String orderReturnUri = "/order/ebook/naver/pay";
                String orderTypePath = orderType.toString().toLowerCase();
                String orderCategoryIdPath = orderCategory.toString().toLowerCase();

                if(orderType.equals(DIRECT)) {
                    redisOrderCache.saveDirectOrderInfo(orderId, new DirectOrderDto(accessToken, ebookId, 1));
                }

                String returnUrl = Path.of(vendorReturnUrl, orderReturnUri, orderTypePath, orderCategoryIdPath).toString();

                NaverCreateOrderRequest naverCreateOrderRequest = NaverCreateOrderRequest.builder()
                        .merchantPayKey(orderId)
                        .productName(ebookName)
                        .productCount(totalQuantity)
                        .totalPayAmount(amountSum)
                        .taxScopeAmount(vatAmount)
                        .taxExScopeAmount(0)
                        .returnUrl(returnUrl)
                        .build();

                if(orderCategory.equals(PRODUCT)) {
                    GiftOrderInfoRequest giftOrderInfoRequest = orderRequest.getGiftOrderInfoRequest();

                }
                
                return new OrderResponse(NAVER, naverCreateOrderRequest);
            }

            case NICEPAY -> {
                String vendorReturnUrl = nicePayProperties.getNiceUrl().getBaseUrl();
                String orderReturnUri = "order/ebook/nicepay/pay";
                String orderTypePath = orderType.toString().toLowerCase();
                String orderCategoryIdPath = orderCategory.toString().toLowerCase();

                if(orderType.equals(DIRECT)) {

                }

                String returnUrl = Path.of(vendorReturnUrl, orderReturnUri, orderTypePath, orderCategoryIdPath).toString();

                NicePayPreOrderResponse nicePayPreOrderResponse = NicePayPreOrderResponse.builder()
                        .orderId(orderId)
                        .amount(amountSum)
                        .goodsName(ebookName)
                        .returnUrl(returnUrl)
                        .build();

                if(orderCategory.equals(PRODUCT)) {
                    GiftOrderInfoRequest giftOrderInfoRequest = orderRequest.getGiftOrderInfoRequest();

                }

                return new OrderResponse(NICEPAY, nicePayPreOrderResponse);
            }
        }
        throw new IllegalArgumentException("unsupported vendor");
    }
}
