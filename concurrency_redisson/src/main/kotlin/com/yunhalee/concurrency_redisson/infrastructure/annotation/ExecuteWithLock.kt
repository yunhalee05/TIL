package com.yunhalee.concurrency_redisson.infrastructure.annotation

import java.util.concurrent.TimeUnit

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class ExecuteWithLock(
    val key: String,
    val timeUnit: TimeUnit,
    val waitTimeAmount: Long = 3000,
    val leaseTimeAmount: Long = 1000
)
