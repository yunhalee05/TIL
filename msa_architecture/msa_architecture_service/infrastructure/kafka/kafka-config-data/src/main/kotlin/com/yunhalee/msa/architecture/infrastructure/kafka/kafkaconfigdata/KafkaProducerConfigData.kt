package com.yunhalee.msa.architecture.infrastructure.kafka.kafkaconfigdata

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration


@ConfigurationProperties(prefix = "kafka-producer-config")
data class KafkaProducerConfigData(
    val keySerializerClass: String,
    val valueSerializerClass: String,
    val compressionType: String,
    val acks: String,
    val batchSize: Int,
    val batchSizeBoostFactor: Int,
    val lingerMs: Int,
    val requestTimeoutMs: Int,
    val retryCount: Int
)