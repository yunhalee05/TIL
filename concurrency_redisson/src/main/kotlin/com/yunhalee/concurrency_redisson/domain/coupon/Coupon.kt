package com.yunhalee.concurrency_redisson.domain.coupon

import com.yunhalee.concurrency_redisson.infrastructure.jpa.BaseEntity
import jakarta.persistence.Entity

@Entity
class Coupon(
    var userId: Long,
    val promotionId: Long,
) : BaseEntity()
