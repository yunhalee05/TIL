package com.yunhalee.msa.architecture.order.domain.application.service.domain.dto.create

import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.util.UUID

data class OrderItem(
    @NotNull
    val productId: UUID,

    @NotNull
    val quantity: Int,

    @NotNull
    val price: BigDecimal,

    @NotNull
    val subTotal: BigDecimal,
)