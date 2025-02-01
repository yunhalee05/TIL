package com.yunhalee.msa.architecture.service.order.messaging.listener.kafka

import com.food.ordering.system.kafka.order.avro.model.PaymentResponseAvroModel
import com.food.ordering.system.kafka.order.avro.model.PaymentStatus
import com.yunhalee.msa.architecture.infrastructure.kafka.kafkaconsumer.KafkaConsumer
import com.yunhalee.msa.architecture.order.domain.application.service.domain.ports.input.message.listener.payment.PaymentResponseMessageListener
import com.yunhalee.msa.architecture.service.order.messaging.mapper.OrderMessagingDataMapper
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import java.util.function.Consumer
import java.util.logging.Logger

@Component
class PaymentResponseKafkaListener(
    private val paymentResponseMessageListener: PaymentResponseMessageListener,
    private val orderMessagingDataMapper: OrderMessagingDataMapper
) : KafkaConsumer<PaymentResponseAvroModel> {

    private val logger = Logger.getLogger(PaymentResponseKafkaListener::class.java.name)

    @KafkaListener(id = "#{kafka-consumer-config.payment-consumer-group-id}", topics = ["#{order-service.payment-response-topic-name}"])
    override fun receive(@Payload messages: List<PaymentResponseAvroModel>,
                         @Header(KafkaHeaders.RECEIVED_KEY) keys: List<String>,
                         @Header(KafkaHeaders.RECEIVED_PARTITION) partitions: List<Int>,
                         @Header(KafkaHeaders.OFFSET) offsets: List<Long>) {
        logger.info("${messages.size} number of payment responses received with keys:${keys}, partitions:${partitions} and offsets: $offsets")
        messages.forEach(Consumer { paymentResponseAvroModel: PaymentResponseAvroModel ->
            if (PaymentStatus.COMPLETED == paymentResponseAvroModel.paymentStatus) {
                logger.info("Processing successful payment for order id: ${paymentResponseAvroModel.orderId}")
                paymentResponseMessageListener.paymentCompleted(orderMessagingDataMapper.paymentResponseAvroModelToPaymentResponse(paymentResponseAvroModel))
            } else if (PaymentStatus.CANCELLED == paymentResponseAvroModel.paymentStatus ||
                PaymentStatus.FAILED == paymentResponseAvroModel.paymentStatus) {
                logger.info("Processing unsuccessful payment for order id: ${paymentResponseAvroModel.orderId}")
                paymentResponseMessageListener.paymentCancelled(orderMessagingDataMapper.paymentResponseAvroModelToPaymentResponse(paymentResponseAvroModel))
            }
        })
    }


}