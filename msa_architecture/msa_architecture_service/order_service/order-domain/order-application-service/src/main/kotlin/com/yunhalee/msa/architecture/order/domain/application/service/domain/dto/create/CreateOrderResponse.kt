package com.yunhalee.msa.architecture.order.domain.application.service.domain.dto.create

import com.yunhalee.msa.architecture.common.domain.valueobject.OrderStatus
import jakarta.validation.constraints.NotNull
import java.util.UUID

data class CreateOrderResponse(
    @NotNull
     val orderTrackingId: UUID,
    @NotNull
     val orderStatus: OrderStatus,
    @NotNull
     val message: String
){

    constructor(builder: Builder) : this(
        builder.orderTrackingId!!,
        builder.orderStatus!!,
        builder.message!!
    )

    companion object {
        fun builder() = Builder()
    }

    class Builder {
        var orderTrackingId: UUID? = null
        var orderStatus: OrderStatus? = null
        var message: String? = null

        fun orderTrackingId(orderTrackingId: UUID) = apply { this.orderTrackingId = orderTrackingId }
        fun orderStatus(orderStatus: OrderStatus) = apply { this.orderStatus = orderStatus }
        fun message(message: String) = apply { this.message = message }

        fun build() = CreateOrderResponse(this)
    }
}