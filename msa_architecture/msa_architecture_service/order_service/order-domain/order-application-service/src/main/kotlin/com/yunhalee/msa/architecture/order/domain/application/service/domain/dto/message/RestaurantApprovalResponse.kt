package com.yunhalee.msa.architecture.order.domain.application.service.domain.dto.message

import com.yunhalee.msa.architecture.common.domain.valueobject.OrderApprovalStatus
import com.yunhalee.msa.architecture.common.domain.valueobject.PaymentStatus
import java.math.BigDecimal
import java.time.Instant

data class RestaurantApprovalResponse(
    val id: String,
    val sagaId: String,
    val orderId: String,
    val restaurantId: String,
    val createdAt: Instant,
    val orderApprovalStatus: OrderApprovalStatus,
    val failureMessages: List<String>
) {

        constructor(builder: Builder) : this(
            builder.id,
            builder.sagaId,
            builder.orderId,
            builder.restaurantId,
            builder.createdAt,
            builder.orderApprovalStatus!!,
            builder.failureMessages
        )

        companion object {
            fun builder() = Builder()
        }

        class Builder {
            var id: String = ""
            var sagaId: String = ""
            var orderId: String = ""
            var restaurantId: String = ""
            var createdAt: Instant = Instant.now()
            var orderApprovalStatus: OrderApprovalStatus? = null
            var failureMessages: List<String> = emptyList()

            fun id(id: String) = apply { this.id = id }
            fun sagaId(sagaId: String) = apply { this.sagaId = sagaId }
            fun orderId(orderId: String) = apply { this.orderId = orderId }
            fun restaurantId(restaurantId: String) = apply { this.restaurantId = restaurantId }
            fun createdAt(createdAt: Instant) = apply { this.createdAt = createdAt }
            fun orderApprovalStatus(orderApprovalStatus: OrderApprovalStatus) = apply { this.orderApprovalStatus = orderApprovalStatus }
            fun failureMessages(failureMessages: List<String>) = apply { this.failureMessages = failureMessages }

            fun build() = RestaurantApprovalResponse(this)
        }
}

