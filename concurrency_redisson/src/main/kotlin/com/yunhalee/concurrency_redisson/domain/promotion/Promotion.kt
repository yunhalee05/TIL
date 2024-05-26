package com.yunhalee.concurrency_redisson.domain.promotion

import com.yunhalee.concurrency_redisson.infrastructure.jpa.BaseEntity
import jakarta.persistence.Entity
import java.time.LocalDateTime

@Entity
class Promotion(
    val price: Long,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
) : BaseEntity()
