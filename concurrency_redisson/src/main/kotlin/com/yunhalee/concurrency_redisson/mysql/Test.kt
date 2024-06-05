package com.yunhalee.concurrency_redisson.mysql

import com.yunhalee.concurrency_redisson.infrastructure.jpa.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import java.time.LocalDateTime

@Entity
class Test(
    @Column(columnDefinition = "datetime(6)")
    val time: LocalDateTime
) : BaseEntity() {
    fun isAfter(time: LocalDateTime) = this.time.isAfter(time)
}
