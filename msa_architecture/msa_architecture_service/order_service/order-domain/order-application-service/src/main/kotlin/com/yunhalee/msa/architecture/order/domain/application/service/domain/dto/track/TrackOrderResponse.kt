package com.yunhalee.msa.architecture.order.domain.application.service.domain.dto.track

import com.yunhalee.msa.architecture.common.domain.valueobject.OrderStatus
import jakarta.validation.constraints.NotNull
import java.util.UUID


data class TrackOrderResponse(
    @NotNull
    val orderTrackingId: UUID? = null,
    @NotNull
    val orderStatus: OrderStatus? = null,
    val failureMessages: List<String>? = null
){
    constructor(builder: Builder) : this(
        builder.orderTrackingId,
        builder.orderStatus,
        builder.failureMessages
    )

    companion object{
        fun builder() = Builder()
    }

    class Builder{
        var orderTrackingId: UUID? = null
        var orderStatus: OrderStatus? = null
        var failureMessages: List<String>? = null

        fun orderTrackingId(orderTrackingId: UUID) = apply { this.orderTrackingId = orderTrackingId }
        fun orderStatus(orderStatus: OrderStatus) = apply { this.orderStatus = orderStatus }
        fun failureMessages(failureMessages: List<String>) = apply { this.failureMessages = failureMessages }

        fun build() = TrackOrderResponse(this)
    }
}

