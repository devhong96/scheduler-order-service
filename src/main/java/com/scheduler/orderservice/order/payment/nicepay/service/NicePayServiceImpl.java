package com.scheduler.orderservice.order.payment.nicepay.service;

import com.scheduler.orderservice.order.client.MemberServiceClient;
import com.scheduler.orderservice.order.common.component.NicePayProperties;
import com.scheduler.orderservice.order.common.component.RedisOrderCache;
import com.scheduler.orderservice.order.common.domain.OrderCategory;
import com.scheduler.orderservice.order.common.domain.OrderType;
import com.scheduler.orderservice.order.common.dto.DirectOrderDto;
import com.scheduler.orderservice.order.exception.custom.NicePayOrderException;
import com.scheduler.orderservice.order.payment.event.direct.vendor.NicePayDirectAfterOrderEvent;
import com.scheduler.orderservice.order.payment.event.direct.vendor.NicePayDirectOrderEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static com.scheduler.orderservice.order.client.dto.MemberFeignDto.StudentResponse;
import static com.scheduler.orderservice.order.payment.nicepay.dto.NicePayRequest.NicePayCancelOrderRequest;
import static com.scheduler.orderservice.order.payment.nicepay.dto.NicePayRequest.NicePayPreOrderRequest;
import static com.scheduler.orderservice.order.payment.nicepay.dto.NicePayResponse.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@Service
@RequiredArgsConstructor
public class NicePayServiceImpl implements NicePayService {

    private final NicePayProperties nicePayProperties;
    private final MemberServiceClient memberServiceClient;
    private final ApplicationEventPublisher eventPublisher;
    private final RedisOrderCache redisOrderCache;

    @Override
    @Transactional
    public void createNicePayOrder(
            String orderId,
            OrderType orderType, OrderCategory orderCategory,
            NicePayPreOrderRequest niceRequest
    ) {

        validateNicePayOrder(niceRequest);

        DirectOrderDto directOrder = redisOrderCache.getDirectOrderInfo(orderId);
        StudentResponse readerInfo = memberServiceClient.getStudentInfo(directOrder.getAccessToken());
        String readerId = readerInfo.getStudentId();
        String username = readerInfo.getUsername();

        NicePayOrderResponse response = getNicePayEbookOrderResponse(niceRequest);

        switch (orderType) {

            case DIRECT : {

                Integer quantity = directOrder.getQuantity();

                eventPublisher.publishEvent(new NicePayDirectOrderEvent(this, readerId, username, quantity, orderCategory, response));
                eventPublisher.publishEvent(new NicePayDirectAfterOrderEvent(this, readerId, username, orderCategory, response));
                break;
            }

            case CART: {
                break;
            }
        }
    }

    private NicePayOrderResponse getNicePayEbookOrderResponse(NicePayPreOrderRequest nicePayPreOrderRequest) {

        String tid = nicePayPreOrderRequest.getTid();
        int amount = nicePayPreOrderRequest.getAmount();

        HttpHeaders headers = getHeaders();

        Map<String, Integer> map = new HashMap<>();
        map.put("amount", amount);

        HttpEntity<Map<String, Integer>> requestEntity = new HttpEntity<>(map, headers);

        ResponseEntity<NicePayOrderResponse> response = new RestTemplate()
                .postForEntity(
                        "https://sandbox-api.nicepay.co.kr/v1/payments" + "/" + tid,
                        requestEntity,
                        NicePayOrderResponse.class
                );
        
        return response.getBody();
    }

    private void validateNicePayOrder(NicePayPreOrderRequest nicePayPreOrderRequest) {

        String tid = nicePayPreOrderRequest.getTid();
        int amount = nicePayPreOrderRequest.getAmount();

        Map<String, Integer> requestBody = new HashMap<>();
        requestBody.put("amount", amount);

        HttpHeaders headers = getHeaders();
        HttpEntity<Map<String, Integer>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<NicePayCheckAmountResponse> response = new RestTemplate()
                .postForEntity(
                        "https://sandbox-api.nicepay.co.kr/v1/check-amount" + "/" + tid,
                        requestEntity,
                        NicePayCheckAmountResponse.class
                );

        NicePayCheckAmountResponse body = response.getBody();

        String resultCode = Objects.requireNonNull(body).getResultCode();
        boolean isValid = body.getIsValid();
        String resultTid = body.getTid();

        if(!isValid && !resultTid.equals(tid) && !resultCode.equals("0000"))
            throw new NicePayOrderException();
    }

    public void cancelEbookNicepayOrder(NicePayCancelOrderRequest nicePayCancelOrderRequest) {

        HttpHeaders headers = getHeaders();

        String cancelAmt = nicePayCancelOrderRequest.getCancelAmt();
        String reason = nicePayCancelOrderRequest.getReason();
        String tid = nicePayCancelOrderRequest.getTid();

        Map<String, String> requestBody = new HashMap<>();

        requestBody.put("cancelAmt", cancelAmt);
        requestBody.put("orderId", UUID.randomUUID().toString());
        requestBody.put("reason", reason);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<NicePayCancelOrderResponse> response = new RestTemplate()
                .postForEntity(
                        "https://sandbox-api.nicepay.co.kr/v1/payments" + "/" + tid + "/" + "cancel",
                        requestEntity,
                        NicePayCancelOrderResponse.class
                );

        log.info(response.toString());

    }

    private HttpHeaders getHeaders() {

        String clientKey = nicePayProperties.getClientKey();
        String secretKey = nicePayProperties.getSecretKey();
        String credentials = clientKey + ":" + secretKey;

        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes(UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        headers.set(AUTHORIZATION, "Basic " + encodedCredentials);
        return headers;
    }
}
