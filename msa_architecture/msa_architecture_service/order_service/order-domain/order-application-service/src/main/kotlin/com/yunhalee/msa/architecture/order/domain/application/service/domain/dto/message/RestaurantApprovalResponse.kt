package com.yunhalee.msa.architecture.order.domain.application.service.domain.dto.message

import com.yunhalee.msa.architecture.common.domain.valueobject.OrderApprovalStatus
import com.yunhalee.msa.architecture.common.domain.valueobject.PaymentStatus
import java.math.BigDecimal
import java.time.Instant

data class RestaurantApprovalResponse (
    val id: String,
    val sagaId: String,
    val orderId: String,
    val restaurantId: String,
    val createdAt: Instant,
    val orderApprovalStatus: OrderApprovalStatus,
    val failureMessages: List<String>
)

