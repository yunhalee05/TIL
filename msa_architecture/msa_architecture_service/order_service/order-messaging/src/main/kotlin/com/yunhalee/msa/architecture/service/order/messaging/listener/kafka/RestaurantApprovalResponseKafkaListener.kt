package com.yunhalee.msa.architecture.service.order.messaging.listener.kafka

import com.food.ordering.system.kafka.order.avro.model.OrderApprovalStatus
import com.food.ordering.system.kafka.order.avro.model.PaymentResponseAvroModel
import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalResponseAvroModel
import com.yunhalee.msa.architecture.infrastructure.kafka.kafkaconsumer.KafkaConsumer
import com.yunhalee.msa.architecture.order.domain.application.service.domain.ports.input.message.listener.restaurantapproval.RestaurantApprovalMessageListener
import com.yunhalee.msa.architecture.service.order.domain.core.entity.Order.Companion.FAILURE_MESSAGE_DELIMITER
import com.yunhalee.msa.architecture.service.order.messaging.mapper.OrderMessagingDataMapper
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import java.util.function.Consumer
import java.util.logging.Logger

@Component
class RestaurantApprovalResponseKafkaListener(
    private val restaurantApprovalMessageListener: RestaurantApprovalMessageListener,
    private val orderMessagingDataMapper: OrderMessagingDataMapper
) : KafkaConsumer<PaymentResponseAvroModel> {

    private val logger = Logger.getLogger(RestaurantApprovalResponseKafkaListener::class.java.name)


    @KafkaListener(id = "#{kafka-consumer-config.restaurant-approval-consumer-group-id}", topics = ["#{order-service.restaurant-approval-response-topic-name}"])
    fun receive(@Payload messages: List<RestaurantApprovalResponseAvroModel>,
                @Header(KafkaHeaders.RECEIVED_KEY) keys: List<String?>,
                @Header(KafkaHeaders.RECEIVED_PARTITION) partitions: List<Int?>,
                @Header(KafkaHeaders.OFFSET) offsets: List<Long?>) {
        logger.info("${messages.size} number of restaurant approval received with keys:${keys}, partitions:${partitions} and offsets: $offsets")

        messages.forEach(Consumer { restaurantApprovalResponseAvroModel: RestaurantApprovalResponseAvroModel ->
            if (OrderApprovalStatus.APPROVED == restaurantApprovalResponseAvroModel.orderApprovalStatus) {
                logger.info("Processing approved order for order id: ${restaurantApprovalResponseAvroModel.orderId)}")
                restaurantApprovalMessageListener.orderApproved(orderMessagingDataMapper.approvalResponseAvroModelToApprovalResponse(restaurantApprovalResponseAvroModel))
            } else if (OrderApprovalStatus.REJECTED == restaurantApprovalResponseAvroModel.orderApprovalStatus) {
                logger.info("Processing rejected order for order id: ${restaurantApprovalResponseAvroModel.orderId}, with failure messages: ${restaurantApprovalResponseAvroModel.failureMessages.joinToString(FAILURE_MESSAGE_DELIMITER)}")
                restaurantApprovalMessageListener.orderRejected(orderMessagingDataMapper.approvalResponseAvroModelToApprovalResponse(restaurantApprovalResponseAvroModel))
            }
        })
    }


}