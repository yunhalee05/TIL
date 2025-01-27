package com.yunhalee.msa.architecture.infrastructure.kafka.kafkaconfigdata

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "kafka-config")
data class KafkaConfigData (
    val bootstrapServers: String,
    val schemaRegistryUrlKey: String,
    val schemaRegistryUrl: String,
    val numOfPartitions: Int,
    val replicationFactor: Short
)