package com.yunhalee.concurrency_practice_consumer.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CouponCountRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public CouponCountRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Long increment() {
        return redisTemplate
                .opsForValue()
                .increment("coupon_count");
    }

    public void clear() {
        redisTemplate.delete("coupon_count");
        redisTemplate.opsForValue().set("coupon_count", "0");
    }
}
