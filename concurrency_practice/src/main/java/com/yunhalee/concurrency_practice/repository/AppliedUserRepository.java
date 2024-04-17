package com.yunhalee.concurrency_practice.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class AppliedUserRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public AppliedUserRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Long add(Long userId) {
        return redisTemplate.opsForSet()
                .add("applied_user", userId.toString());
    }

}
