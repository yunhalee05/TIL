package com.yunhalee.concurrency_redisson.repository.redis

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component


@Component
class RedisKeyRepository(private val redisTemplate: RedisTemplate<String, String>) {
    fun getKey(key: Long): Long? {
        return redisTemplate.opsForSet()
            .add("couponKey", key.toString())
    }
}

