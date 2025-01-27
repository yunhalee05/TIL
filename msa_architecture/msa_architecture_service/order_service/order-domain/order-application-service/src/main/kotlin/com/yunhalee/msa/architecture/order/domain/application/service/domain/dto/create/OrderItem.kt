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
) {
    constructor(builder: Builder): this(
        builder.productId,
        builder.quantity,
        builder.price,
        builder.subTotal
    )

    companion object {
        fun builder() = Builder()
    }

    class Builder {
        lateinit var productId: UUID
        var quantity: Int = 0
        lateinit var price: BigDecimal
        lateinit var subTotal: BigDecimal

        fun productId(productId: UUID) = apply { this.productId = productId }
        fun quantity(quantity: Int) = apply { this.quantity = quantity }
        fun price(price: BigDecimal) = apply { this.price = price }
        fun subTotal(subTotal: BigDecimal) = apply { this.subTotal = subTotal }

        fun build() = OrderItem(this)
    }
}