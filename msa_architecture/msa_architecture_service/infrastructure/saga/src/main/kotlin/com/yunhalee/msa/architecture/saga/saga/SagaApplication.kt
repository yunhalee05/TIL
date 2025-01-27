package com.yunhalee.msa.architecture.saga.saga

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SagaApplication

fun main(args: Array<String>) {
	runApplication<SagaApplication>(*args)
}
