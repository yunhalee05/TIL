package com.yunhalee.concurrency_coroutine

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ConcurrencyCoroutineApplication

fun main(args: Array<String>) {
    runApplication<ConcurrencyCoroutineApplication>(*args)
}
