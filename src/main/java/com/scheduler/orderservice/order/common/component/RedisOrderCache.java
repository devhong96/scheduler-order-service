package com.scheduler.orderservice.order.common.component;

import com.scheduler.orderservice.order.common.dto.DirectOrderDto;
import com.scheduler.orderservice.order.common.dto.KakaoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import static java.util.concurrent.TimeUnit.SECONDS;

@Repository
@RequiredArgsConstructor
public class RedisOrderCache {

    private static final String DIRECT_ORDER_PREFIX = "direct:";
    private static final String KAKAO_ORDER_PREFIX = "kakao:";
    private static final String NAVER_ORDER_PREFIX = "naver:";
    private static final String NICEPAY_ORDER_PREFIX = "nicepay:";
    private static final long EXPIRATION_TIME = 5 * 60; // 5분 (단위: 초)

    private final RedisTemplate<String, Object> redisTemplate;

    public void saveDirectOrderInfo(String orderId, DirectOrderDto directOrderDto) {
        String key = DIRECT_ORDER_PREFIX + orderId;
        redisTemplate.opsForValue().set(key, directOrderDto, EXPIRATION_TIME, SECONDS);
    }

    public DirectOrderDto getDirectOrderInfo(String orderId) {
        String key = DIRECT_ORDER_PREFIX + orderId;
        return (DirectOrderDto) redisTemplate.opsForValue().get(key);
    }

    public void saveKakaoOrderInfo(String orderId, KakaoDto kakaoDto) {
        String key = KAKAO_ORDER_PREFIX + orderId;
        redisTemplate.opsForValue().set(key, kakaoDto, EXPIRATION_TIME, SECONDS);
    }

    public KakaoDto getKakaoOrderInfo(String orderId) {
        String key = KAKAO_ORDER_PREFIX + orderId;
        return (KakaoDto) redisTemplate.opsForValue().get(key);
    }

    public void saveNaverOrderInfo(String orderId, String authNum) {
        String key = NAVER_ORDER_PREFIX + orderId;
        redisTemplate.opsForValue().set(key, authNum, EXPIRATION_TIME, SECONDS);
    }

    public String getNaverOrderInfo(String orderId) {
        String key = NAVER_ORDER_PREFIX + orderId;
        return (String) redisTemplate.opsForValue().get(key);
    }

    public void saveNicePayOrderInfo(String orderId, String authNum) {
        String key = NICEPAY_ORDER_PREFIX + orderId;
        redisTemplate.opsForValue().set(key, authNum, EXPIRATION_TIME, SECONDS);
    }

    public String getNicePayOrderInfo(String orderId) {
        String key = NICEPAY_ORDER_PREFIX + orderId;
        return (String) redisTemplate.opsForValue().get(key);
    }



}
