package com.yunhalee.concurrency_redisson.repository.coupon

import com.yunhalee.concurrency_redisson.domain.coupon.Coupon
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CouponRepository : JpaRepository<Coupon, Long> {
    fun countAllByPromotionId(promotionId: Long): Int
}
