package com.yunhalee.msa.architecture.order.domain.application.service.domain.ports.output.repository

import com.yunhalee.msa.architecture.common.domain.valueobject.OrderId
import com.yunhalee.msa.architecture.service.order.domain.core.entity.Order
import com.yunhalee.msa.architecture.service.order.domain.core.valueobject.TrackingId
import java.util.Optional

interface OrderRepository {
    fun save(order: Order): Order

    fun findById(orderId: OrderId): Optional<Order>

    fun findByTrackingId(trackingId: TrackingId): Optional<Order>

}