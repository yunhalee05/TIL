package com.yunhalee.msa.architecture.infrastructure.kafka.kafkaproducer.service.impl

import com.yunhalee.msa.architecture.infrastructure.kafka.kafkaproducer.exception.KafkaProducerException
import com.yunhalee.msa.architecture.infrastructure.kafka.kafkaproducer.service.KafkaProducer
import jakarta.annotation.PreDestroy
import org.apache.avro.specific.SpecificRecordBase
import org.springframework.kafka.KafkaException
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Component
import java.io.Serializable
import java.util.concurrent.CompletableFuture
import java.util.logging.Logger


@Component
class KafkaProducerImpl<K : Serializable, V : SpecificRecordBase>(private val kafkaTemplate: KafkaTemplate<K, V>) : KafkaProducer<K, V> {

    private val logger = Logger.getLogger(KafkaProducerImpl::class.java.name)
    override fun send(topicName: String, key: K, message: V, callback: CompletableFuture<SendResult<K, V>>) {
        logger.info("Sending message=$message to topic=$topicName")
        try {
            val kafkaResultFuture: CompletableFuture<SendResult<K, V>> = kafkaTemplate.send(topicName, key, message)
            kafkaResultFuture.whenComplete { result, exception ->
                if (exception != null) {
                    logger.warning("Error on kafka producer with key: $key, message: $message and exception: $exception.message")
                    callback.completeExceptionally(KafkaProducerException("Error on kafka producer with key: $key and message: $message"))
                } else {
                    callback.complete(result)
                }
            }
        } catch (e: KafkaException) {
            logger.warning("Error on kafka producer with key: $key, message: $message and exception: ${e.message}" )
            throw KafkaProducerException("Error on kafka producer with key: $key and message: $message")
        }
    }


    @PreDestroy
    fun close() {
        logger.info("Closing kafka producer!")
        kafkaTemplate.destroy()
    }


}

