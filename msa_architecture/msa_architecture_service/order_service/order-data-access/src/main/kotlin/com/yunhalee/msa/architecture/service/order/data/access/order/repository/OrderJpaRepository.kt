package com.yunhalee.msa.architecture.service.order.data.access.order.repository

import com.yunhalee.msa.architecture.service.order.data.access.order.entity.OrderEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface OrderJpaRepository: JpaRepository<OrderEntity, UUID>{

    fun findByTrackingId(trackingId: UUID): OrderEntity?
}