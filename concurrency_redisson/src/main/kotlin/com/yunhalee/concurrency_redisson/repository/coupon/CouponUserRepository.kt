package com.yunhalee.concurrency_redisson.repository.coupon

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component


@Component
class CouponUserRepository(private val redisTemplate: RedisTemplate<String, String>) {
    fun add(userId: Long, promotionId: Long): Long? {
        return redisTemplate.opsForSet()
            .add("coupon_user_$promotionId", userId.toString())
    }
}
