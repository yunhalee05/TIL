package com.yunhalee.concurrency_redisson.mysql

import com.yunhalee.concurrency_redisson.domain.coupon.Coupon
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface TestRepository : JpaRepository<Test, Long> {

    fun findAllByTimeIsLessThanEqual(after: LocalDateTime): List<Test>
}
