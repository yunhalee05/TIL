package com.yunhalee.concurrency_redisson

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ConcurrencyRedissonApplication

fun main(args: Array<String>) {
    runApplication<ConcurrencyRedissonApplication>(*args)
}
