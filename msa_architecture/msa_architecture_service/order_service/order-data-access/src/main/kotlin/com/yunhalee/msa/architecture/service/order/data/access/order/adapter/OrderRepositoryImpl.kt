package com.yunhalee.msa.architecture.service.order.data.access.order.adapter

import com.yunhalee.msa.architecture.common.domain.valueobject.OrderId
import com.yunhalee.msa.architecture.order.domain.application.service.domain.ports.output.repository.OrderRepository
import com.yunhalee.msa.architecture.service.order.data.access.order.mapper.OrderDataAccessMapper
import com.yunhalee.msa.architecture.service.order.data.access.order.repository.OrderJpaRepository
import com.yunhalee.msa.architecture.service.order.domain.core.entity.Order
import com.yunhalee.msa.architecture.service.order.domain.core.valueobject.TrackingId
import org.springframework.stereotype.Component
import java.util.Optional

@Component
class OrderRepositoryImpl(
    private val orderJpaRepository: OrderJpaRepository,
    private val orderDataAccessMapper: OrderDataAccessMapper
) : OrderRepository {
    override fun save(order: Order): Order {
        val orderEntity = orderJpaRepository.save(orderDataAccessMapper.orderToOrderEntity(order))
        return orderDataAccessMapper.orderEntityToOrder(orderEntity)
    }

    override fun findById(orderId: OrderId): Optional<Order> {
        return orderJpaRepository.findById(orderId.getValue()).map { orderDataAccessMapper.orderEntityToOrder(it) }
    }

    override fun findByTrackingId(trackingId: TrackingId): Optional<Order> {
        return orderJpaRepository.findByTrackingId(trackingId.getValue())?.let { Optional.of(orderDataAccessMapper.orderEntityToOrder(it)) } ?: Optional.empty()
    }
}