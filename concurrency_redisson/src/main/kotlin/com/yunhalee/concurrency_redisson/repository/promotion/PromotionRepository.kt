package com.yunhalee.concurrency_redisson.repository.promotion

import com.yunhalee.concurrency_redisson.domain.promotion.Promotion
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PromotionRepository : JpaRepository<Promotion, Long>
