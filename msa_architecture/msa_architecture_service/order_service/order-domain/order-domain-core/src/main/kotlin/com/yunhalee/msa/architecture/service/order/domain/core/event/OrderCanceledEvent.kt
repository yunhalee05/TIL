package com.yunhalee.msa.architecture.service.order.domain.core.event

import com.yunhalee.msa.architecture.common.domain.event.DomainEvent
import com.yunhalee.msa.architecture.service.order.domain.core.entity.Order
import java.time.ZonedDateTime

class OrderCanceledEvent(
    override var order: Order,
    override var createdAt: ZonedDateTime,
) : OrderEvent()