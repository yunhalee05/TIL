package com.yunhalee.msa.architecture.service.order.messaging.publisher.kafka

import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalRequestAvroModel
import com.yunhalee.msa.architecture.infrastructure.kafka.kafkaproducer.KafkaMessageHelper
import com.yunhalee.msa.architecture.infrastructure.kafka.kafkaproducer.service.KafkaProducer
import com.yunhalee.msa.architecture.order.domain.application.service.domain.config.OrderServiceConfigData
import com.yunhalee.msa.architecture.order.domain.application.service.domain.ports.output.message.publisher.payment.PayOrderMessagePublisher
import com.yunhalee.msa.architecture.service.order.domain.core.event.OrderPaidEvent
import com.yunhalee.msa.architecture.service.order.messaging.mapper.OrderMessagingDataMapper
import org.springframework.stereotype.Component
import java.util.logging.Logger

@Component
class PayOrderKafkaMessagePublisher(
    private val orderMessageDataMapper: OrderMessagingDataMapper,
    private val orderServiceConfigData: OrderServiceConfigData,
    private val kafkaProducer: KafkaProducer<String, RestaurantApprovalRequestAvroModel>,
    private val orderKafkaMessageHelper: KafkaMessageHelper
) : PayOrderMessagePublisher {

    private val logger = Logger.getLogger(PayOrderKafkaMessagePublisher::class.java.name)
    override fun publish(event: OrderPaidEvent) {
        val orderId = event.order.id!!.getValue().toString()
        logger.info("Received order paid event for order id: $orderId")
        try {
            val restaurantApprovalRequestAvroModel = orderMessageDataMapper.orderPaidEventToRestaurantApprovalRequestAvroModel(event)
            kafkaProducer.send(orderServiceConfigData.restaurantApprovalRequestTopicName, orderId, restaurantApprovalRequestAvroModel, orderKafkaMessageHelper.getKafkaCallback(orderServiceConfigData.paymentResponseTopicName, restaurantApprovalRequestAvroModel, orderId, "RestaurantApprovalRequestMessagePublisher"))
            logger.info("RestaurantApprovalRequestAvroModel sent to Kafka for order id: $orderId")
        } catch (e: Exception) {
            logger.warning("Error while sending RestaurantApprovalRequestAvroModel message to kafka with order id: $orderId, error: ${e.message}")
        }

    }

}