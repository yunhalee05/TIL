package com.yunhalee.concurrency_redisson.service.coupon

import com.yunhalee.concurrency_redisson.domain.coupon.Coupon
import com.yunhalee.concurrency_redisson.repository.coupon.CouponRepository
import org.springframework.stereotype.Service

@Service
class CouponService(
    private val couponRepository: CouponRepository
) {

    fun issueCoupon(userId: Long, promotionId: Long) {
        couponRepository.save(Coupon(userId = userId, promotionId = promotionId))
    }
}
