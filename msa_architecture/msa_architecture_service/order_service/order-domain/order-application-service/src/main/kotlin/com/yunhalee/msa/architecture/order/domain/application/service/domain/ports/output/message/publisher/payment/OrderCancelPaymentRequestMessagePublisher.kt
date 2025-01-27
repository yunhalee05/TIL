package com.yunhalee.msa.architecture.order.domain.application.service.domain.ports.output.message.publisher.payment

import com.yunhalee.msa.architecture.common.domain.event.publisher.DomainEventPublisher
import com.yunhalee.msa.architecture.service.order.domain.core.event.OrderCanceledEvent

interface OrderCancelPaymentRequestMessagePublisher: DomainEventPublisher<OrderCanceledEvent> {
}