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
) {

    constructor(builder: Builder): this(
        builder.customerId,
        builder.restaurantId,
        builder.price,
        builder.items,
        builder.address
    )

    companion object {
        fun builder() = Builder()
    }

    class Builder {
        lateinit var customerId: UUID
        lateinit var restaurantId: UUID
        lateinit var price: BigDecimal
        lateinit var items: List<OrderItem>
        lateinit var address: OrderAddress

        fun customerId(customerId: UUID) = apply { this.customerId = customerId }
        fun restaurantId(restaurantId: UUID) = apply { this.restaurantId = restaurantId }
        fun price(price: BigDecimal) = apply { this.price = price }
        fun items(items: List<OrderItem>) = apply { this.items = items }
        fun address(address: OrderAddress) = apply { this.address = address }

        fun build() = CreateOrderCommand(this)
    }
}