package com.yunhalee.msa.architecture.infrastructure.kafka.kafkamodel

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KafkaModelApplication

fun main(args: Array<String>) {
	runApplication<KafkaModelApplication>(*args)
}
