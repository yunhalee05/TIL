package com.yunhalee.concurrency_redisson.service.coupon.redis

import com.yunhalee.concurrency_redisson.service.coupon.CouponService
import org.redisson.api.RedissonClient
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit


@Component
class RedissonCouponService(
    private val redissonClient: RedissonClient,
    private val couponService: CouponService
) {
    fun issueCoupon(promotionId: Long, userId: Long, count: Int) {
        val lock = redissonClient.getLock(promotionId.toString())
        try {
            val available = lock.tryLock(10, 1, TimeUnit.SECONDS)
            if (!available) {
                println("lock 획득에 실패하였습니다.")
                return
            }
            couponService.issueCoupon(userId, promotionId, count)
        } catch (e: InterruptedException) {
            throw RuntimeException("interruptedException이 발생했습니다.", e)
        } finally {
            lock.unlock()
        }
    }
}

