package com.yunhalee.concurrency_redisson.infrastructure

import org.springframework.cache.annotation.CacheEvict
import org.springframework.transaction.annotation.Transactional

annotation class ExecuteWithLock(
    val key: String,
    val timeUnit: Long = 3000,
    val waitTimeAmount: Long = 3000,
    val leaseTimeAmount: Long = 1000
)