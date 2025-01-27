package com.yunhalee.msa.architecture.order.domain.application.service.domain.dto.track

import com.yunhalee.msa.architecture.common.domain.valueobject.OrderStatus
import jakarta.validation.constraints.NotNull
import java.util.UUID


data class TrackOrderResponse(
    @NotNull
    private val orderTrackingId: UUID? = null,
    @NotNull
    private val orderStatus: OrderStatus? = null,
    private val failureMessages: List<String>? = null
)

