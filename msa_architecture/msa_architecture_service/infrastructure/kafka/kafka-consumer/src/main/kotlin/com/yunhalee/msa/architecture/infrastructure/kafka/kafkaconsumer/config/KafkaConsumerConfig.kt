package com.yunhalee.msa.architecture.infrastructure.kafka.kafkaconsumer.config

import com.yunhalee.msa.architecture.infrastructure.kafka.kafkaconfigdata.KafkaConfigData
import com.yunhalee.msa.architecture.infrastructure.kafka.kafkaconfigdata.KafkaConsumerConfigData
import org.apache.avro.specific.SpecificRecordBase
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.KafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer
import java.io.Serializable

@Configuration
class KafkaConsumerConfig<K : Serializable, V : SpecificRecordBase>(
    private val kafkaConfigData: KafkaConfigData,
    private val kafkaConsumerConfigData: KafkaConsumerConfigData
) {

    @Bean
    fun consumerConfigs(): Map<String, Any> {
        val props: MutableMap<String, Any> = HashMap()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafkaConfigData.bootstrapServers
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = kafkaConsumerConfigData.keyDeserializer
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = kafkaConsumerConfigData.valueDeserializer
        props[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = kafkaConsumerConfigData.autoOffsetReset
        props[kafkaConfigData.schemaRegistryUrlKey] = kafkaConfigData.schemaRegistryUrl
        props[kafkaConsumerConfigData.specificAvroReaderKey] = kafkaConsumerConfigData.specificAvroReader
        props[ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG] = kafkaConsumerConfigData.sessionTimeoutMs
        props[ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG] = kafkaConsumerConfigData.heartbeatIntervalMs
        props[ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG] = kafkaConsumerConfigData.maxPollIntervalMs
        props[ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG] = kafkaConsumerConfigData.maxPartitionFetchBytesDefault * kafkaConsumerConfigData.maxPartitionFetchBytesBoostFactor
        props[ConsumerConfig.MAX_POLL_RECORDS_CONFIG] = kafkaConsumerConfigData.maxPollRecords
        return props
    }

    @Bean
    fun consumerFactory(): ConsumerFactory<K, V> {
        return DefaultKafkaConsumerFactory(consumerConfigs())
    }

    @Bean
    fun kafkaListenerContainerFactory(): KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<K, V>> {
        val factory = ConcurrentKafkaListenerContainerFactory<K, V>()
        factory.consumerFactory = consumerFactory()
        factory.isBatchListener = kafkaConsumerConfigData.batchListener
        factory.setConcurrency(kafkaConsumerConfigData.concurrencyLevel)
        factory.setAutoStartup(kafkaConsumerConfigData.autoStartup)
        factory.containerProperties.pollTimeout = kafkaConsumerConfigData.pollTimeoutMs
        return factory
    }
}

