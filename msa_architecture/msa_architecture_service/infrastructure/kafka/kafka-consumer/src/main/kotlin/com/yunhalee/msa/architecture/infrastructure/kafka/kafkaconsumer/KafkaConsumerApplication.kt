package com.yunhalee.msa.architecture.infrastructure.kafka.kafkaconsumer

import com.yunhalee.msa.architecture.infrastructure.kafka.kafkaconfigdata.KafkaConfigData
import com.yunhalee.msa.architecture.infrastructure.kafka.kafkaconfigdata.KafkaConsumerConfigData
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(
    KafkaConsumerConfigData::class,
    KafkaConfigData::class
)
class KafkaConsumerApplication

fun main(args: Array<String>) {
    runApplication<KafkaConsumerApplication>(*args)
}
