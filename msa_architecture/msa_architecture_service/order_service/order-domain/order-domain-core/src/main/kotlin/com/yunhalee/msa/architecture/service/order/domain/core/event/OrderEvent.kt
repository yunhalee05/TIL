package com.yunhalee.msa.architecture.service.order.domain.core.event

import com.yunhalee.msa.architecture.common.domain.event.DomainEvent
import com.yunhalee.msa.architecture.service.order.domain.core.entity.Order
import java.time.ZonedDateTime

abstract class OrderEvent : DomainEvent<Order> {
    open lateinit var order: Order
    open lateinit var createdAt: ZonedDateTime
}