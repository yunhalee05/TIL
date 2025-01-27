package com.yunhalee.msa.architecture.infrastructure.kafka.kafkaconfigdata

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(
    KafkaProducerConfigData::class,
    KafkaConsumerConfigData::class,
    KafkaConfigData::class
)
class KafkaConfigDataApplication

fun main(args: Array<String>) {
    runApplication<KafkaConfigDataApplication>(*args)
}
