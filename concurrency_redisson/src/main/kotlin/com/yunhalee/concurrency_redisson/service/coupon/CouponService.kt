package com.yunhalee.concurrency_redisson.service.coupon

import com.yunhalee.concurrency_redisson.domain.coupon.Coupon
import com.yunhalee.concurrency_redisson.repository.coupon.CouponRepository
import org.springframework.stereotype.Service

@Service
class CouponService(
    private val couponRepository: CouponRepository
) {

    fun issueCoupon(userId: Long, promotionId: Long, count: Int = 1) {
        if (couponRepository.countAllByPromotionId(promotionId) >= count) {
            throw RuntimeException("이미 발급 가능한 쿠폰 갯수가 소진되었습니다.")
        }
        couponRepository.save(Coupon(userId = userId, promotionId = promotionId))
    }
}
