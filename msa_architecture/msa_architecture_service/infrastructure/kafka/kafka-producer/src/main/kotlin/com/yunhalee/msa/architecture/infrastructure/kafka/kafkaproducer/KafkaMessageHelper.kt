package com.yunhalee.msa.architecture.infrastructure.kafka.kafkaproducer

import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture
import java.util.logging.Logger

@Component
class KafkaMessageHelper {

    private val logger = Logger.getLogger(KafkaMessageHelper::class.java.name)
    fun <T> getKafkaCallback(responseTopicName: String, avroModel: T, orderId: String?, avroModelName: String): CompletableFuture<SendResult<String, T>> {
        return CompletableFuture<SendResult<String, T>>().whenComplete{ result, exception ->
            if (exception != null) {
                logger.warning("Error on kafka producer with key: $orderId message: ${avroModel.toString()} and exception: ${exception.message}")
            } else {
                logger.info("Received successful response from Kafka for order id: $orderId" +
                    " Topic: ${result!!.recordMetadata.topic()} Partition: ${result.recordMetadata.partition()} Offset: ${result.recordMetadata.offset()} Timestamp: ${result.recordMetadata.timestamp()}")
            }
        }
    }
}

