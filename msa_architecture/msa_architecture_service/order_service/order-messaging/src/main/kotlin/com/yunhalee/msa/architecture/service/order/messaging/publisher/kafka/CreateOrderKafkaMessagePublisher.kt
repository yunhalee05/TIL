package com.yunhalee.msa.architecture.service.order.messaging.publisher.kafka

import com.food.ordering.system.kafka.order.avro.model.PaymentRequestAvroModel
import com.yunhalee.msa.architecture.infrastructure.kafka.kafkaproducer.KafkaMessageHelper
import com.yunhalee.msa.architecture.infrastructure.kafka.kafkaproducer.service.KafkaProducer
import com.yunhalee.msa.architecture.order.domain.application.service.domain.config.OrderServiceConfigData
import com.yunhalee.msa.architecture.order.domain.application.service.domain.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher
import com.yunhalee.msa.architecture.service.order.domain.core.event.OrderCreateEvent
import com.yunhalee.msa.architecture.service.order.messaging.mapper.OrderMessagingDataMapper
import org.springframework.stereotype.Component
import java.util.logging.Logger

@Component
class CreateOrderKafkaMessagePublisher(
    private val orderMessageDataMapper: OrderMessagingDataMapper,
    private val orderServiceConfigData: OrderServiceConfigData,
    private val kafkaProducer: KafkaProducer<String, PaymentRequestAvroModel>,
    private val orderKafkaMessageHelper: KafkaMessageHelper
) : OrderCreatedPaymentRequestMessagePublisher {

    private val logger = Logger.getLogger(CreateOrderKafkaMessagePublisher::class.java.name)
    override fun publish(event: OrderCreateEvent) {
        val orderId = event.order.id!!.getValue().toString()
        logger.info("Received order created event for order id: $orderId")
        try {
            val paymentRequestAvroModel = orderMessageDataMapper.orderCreatedEventToPaymentRequestAvroModel(event)
            kafkaProducer.send(orderServiceConfigData.paymentRequestTopicName, orderId, paymentRequestAvroModel, orderKafkaMessageHelper.getKafkaCallback(orderServiceConfigData.paymentResponseTopicName, paymentRequestAvroModel, orderId, "PaymentRequestAvroModel"))
            logger.info("PaymentRequestAvroModel sent to Kafka for order id: $orderId")
        } catch (e: Exception) {
            logger.warning("Error while sending PaymentRequestAvroModel message to kafka with order id: $orderId, error: ${e.message}")
        }

    }

}