package com.yunhalee.msa.architecture.order.domain.application.service.domain.dto.create

import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.util.UUID

data class CreateOrderCommand(
    @NotNull
    val customerId: UUID,

    @NotNull
    val restaurantId: UUID,

    @NotNull
    val price: BigDecimal,

    @NotNull
    val items: List<OrderItem>,

    @NotNull
    val address: OrderAddress
)