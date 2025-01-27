package com.yunhalee.msa.architecture.order.domain.application.service.domain

import com.yunhalee.msa.architecture.order.domain.application.service.domain.dto.create.CreateOrderCommand
import com.yunhalee.msa.architecture.order.domain.application.service.domain.dto.create.CreateOrderResponse
import com.yunhalee.msa.architecture.order.domain.application.service.domain.mapper.OrderDataMapper
import com.yunhalee.msa.architecture.order.domain.application.service.domain.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher
import org.springframework.stereotype.Component
import java.util.logging.Logger

// Option 1 : OrderCreateCommandHandler
@Component
class OrderCreateCommandHandlerV1(
    private val orderCreateHelper: OrderCreateHelper,
    private val orderDataMapper: OrderDataMapper,
    private val orderCreatePaymentRequestMessagePublisher: OrderCreatedPaymentRequestMessagePublisher
) {

    private val logger = Logger.getLogger(OrderCreateCommandHandlerV1::class.java.name)

    fun createOrder(createOrderCommand: CreateOrderCommand): CreateOrderResponse {
        val orderCreateEvent = orderCreateHelper.persistOrder(createOrderCommand)
        logger.info("order is created with id : ${orderCreateEvent.order.id}")
        orderCreatePaymentRequestMessagePublisher.publish(orderCreateEvent)
        return orderDataMapper.orderToCreateOrderResponse(orderCreateEvent.order, "Order is created successfully")
    }
}