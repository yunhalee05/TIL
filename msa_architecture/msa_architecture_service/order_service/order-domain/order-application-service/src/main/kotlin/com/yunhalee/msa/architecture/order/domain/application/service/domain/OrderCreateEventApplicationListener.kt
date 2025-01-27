package com.yunhalee.msa.architecture.order.domain.application.service.domain

import com.yunhalee.msa.architecture.order.domain.application.service.domain.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher
import com.yunhalee.msa.architecture.service.order.domain.core.event.OrderCreateEvent
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionalEventListener

@Component
class OrderCreateEventApplicationListener(
    private val orderCreatedPaymentRequestMessagePublisher: OrderCreatedPaymentRequestMessagePublisher
) {
    @TransactionalEventListener
    fun processOrderCreateEvent(orderCreateEvent: OrderCreateEvent) {
        orderCreatedPaymentRequestMessagePublisher.publish(orderCreateEvent)
    }
}