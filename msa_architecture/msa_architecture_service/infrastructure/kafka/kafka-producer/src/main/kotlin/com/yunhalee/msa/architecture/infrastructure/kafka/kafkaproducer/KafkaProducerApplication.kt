package com.yunhalee.msa.architecture.infrastructure.kafka.kafkaproducer

import com.yunhalee.msa.architecture.infrastructure.kafka.kafkaconfigdata.KafkaConfigData
import com.yunhalee.msa.architecture.infrastructure.kafka.kafkaconfigdata.KafkaProducerConfigData
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(
	KafkaConfigData::class,
	KafkaProducerConfigData::class
)
class KafkaProducerApplication

fun main(args: Array<String>) {
	runApplication<KafkaProducerApplication>(*args)
}
