package com.yunhalee.msa.architecture.service.order.domain.core.event

import com.yunhalee.msa.architecture.service.order.domain.core.entity.Order
import java.time.ZonedDateTime

class OrderCreateEvent(
    override var order: Order,
    override var createdAt: ZonedDateTime,
): OrderEvent()