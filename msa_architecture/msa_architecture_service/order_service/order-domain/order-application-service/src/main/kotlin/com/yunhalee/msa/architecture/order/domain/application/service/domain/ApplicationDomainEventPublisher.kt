package com.yunhalee.msa.architecture.order.domain.application.service.domain

import com.yunhalee.msa.architecture.common.domain.event.publisher.DomainEventPublisher
import com.yunhalee.msa.architecture.service.order.domain.core.event.OrderCreateEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.ApplicationEventPublisherAware
import org.springframework.stereotype.Component
import java.util.logging.Logger


// Option 2: ApplicationDomainEventPublisher

@Component
class ApplicationDomainEventPublisher(private val applicationEventPublisher: ApplicationEventPublisher) : DomainEventPublisher<OrderCreateEvent> {

    private val logger = Logger.getLogger(ApplicationDomainEventPublisher::class.java.name)

    override fun publish(event: OrderCreateEvent) {
        this.applicationEventPublisher.publishEvent(event)
        logger.info("OrderCreateEvent is published for id : ${event.order.id}")
    }
}