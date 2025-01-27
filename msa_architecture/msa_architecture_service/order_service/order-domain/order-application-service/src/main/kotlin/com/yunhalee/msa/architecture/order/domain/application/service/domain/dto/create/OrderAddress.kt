package com.yunhalee.msa.architecture.order.domain.application.service.domain.dto.create

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.NotNull


data class OrderAddress(
    @NotNull
    @Max(value = 50)
    val street: String,

    @NotNull
    @Max(value = 10)
    val postalCode: String,

    @NotNull
    @Max(value = 50)
    val city: String,
) {
    constructor(builder: Builder): this(
        builder.street,
        builder.postalCode,
        builder.city
    )

    companion object {
        fun builder() = Builder()
    }

    class Builder {
        lateinit var street: String
        lateinit var postalCode: String
        lateinit var city: String

        fun street(street: String) = apply { this.street = street }
        fun postalCode(postalCode: String) = apply { this.postalCode = postalCode }
        fun city(city: String) = apply { this.city = city }

        fun build() = OrderAddress(this)
    }
}