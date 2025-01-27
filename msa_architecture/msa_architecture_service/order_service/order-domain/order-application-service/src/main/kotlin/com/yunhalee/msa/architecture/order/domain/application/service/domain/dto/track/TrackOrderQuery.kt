package com.yunhalee.msa.architecture.order.domain.application.service.domain.dto.track

import jakarta.validation.constraints.NotNull
import java.util.UUID

data class TrackOrderQuery(
    @NotNull
    private val orderTrackingId: UUID? = null
)