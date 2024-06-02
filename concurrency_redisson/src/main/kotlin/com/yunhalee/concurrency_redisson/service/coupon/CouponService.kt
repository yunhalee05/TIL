package com.yunhalee.concurrency_redisson.service.coupon

import com.yunhalee.concurrency_redisson.domain.coupon.Coupon
import com.yunhalee.concurrency_redisson.infrastructure.annotation.ExecuteWithLock
import com.yunhalee.concurrency_redisson.repository.coupon.CouponRepository
import com.yunhalee.concurrency_redisson.service.coupon.redis.RedissonCouponService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.TimeUnit

@Service
class CouponService(
    private val couponRepository: CouponRepository,
) {

    @Transactional
    fun issueCoupon(userId: Long, promotionId: Long, count: Int = 1) {
        if (couponRepository.existsByUserIdAndPromotionId(userId, promotionId)) {
            throw RuntimeException("해당 프로모션 쿠폰은 한사람 당 하나만 발행이 가능합니다. 이미 발급된 쿠폰이 존재합니다.")
        }
        if (couponRepository.countAllByPromotionId(promotionId) >= count) {
            throw RuntimeException("이미 발급 가능한 쿠폰 갯수가 소진되었습니다.")
        }
        couponRepository.save(Coupon(userId = userId, promotionId = promotionId))
    }

    @ExecuteWithLock(key = "#promotionId", waitTimeAmount = 3, leaseTimeAmount = 1, timeUnit = TimeUnit.SECONDS)
    @Transactional
    fun issueCoupon2(userId: Long, promotionId: Long, count: Int = 1) {
        println("쿠폰 발급 시작  userId: $userId, promotionId: $promotionId, count: $count")
        if (couponRepository.existsByUserIdAndPromotionId(userId, promotionId)) {
            println("쿠폰 발급 예외 : 중복 사용자")
            throw RuntimeException("해당 프로모션 쿠폰은 한사람 당 하나만 발행이 가능합니다. 이미 발급된 쿠폰이 존재합니다.")
        }
        val cnt = couponRepository.countAllByPromotionId(promotionId)
        println("cnt:$cnt")
        if (cnt >= count) {
            println("쿠폰 발급 예외 : 발급 횟수 초과: $cnt")
            throw RuntimeException("이미 발급 가능한 쿠폰 갯수가 소진되었습니다.")
        }
        couponRepository.save(Coupon(userId = userId, promotionId = promotionId))
        println("쿠폰 발급 완료  userId: $userId, promotionId: $promotionId, count: $count + 발급 카운트 ${couponRepository.countAllByPromotionId(promotionId)}")
    }

}
