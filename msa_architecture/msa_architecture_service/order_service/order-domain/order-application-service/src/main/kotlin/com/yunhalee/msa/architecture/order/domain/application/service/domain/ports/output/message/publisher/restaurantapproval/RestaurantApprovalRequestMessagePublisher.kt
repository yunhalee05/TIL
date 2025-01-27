package com.yunhalee.msa.architecture.order.domain.application.service.domain.ports.output.message.publisher.restaurantapproval

import com.yunhalee.msa.architecture.common.domain.event.publisher.DomainEventPublisher
import com.yunhalee.msa.architecture.service.order.domain.core.event.OrderCreateEvent
import java.util.function.BiConsumer

interface RestaurantApprovalRequestMessagePublisher: DomainEventPublisher<OrderCreateEvent> {

}