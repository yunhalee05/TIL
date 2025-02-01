package com.yunhalee.msa.architecture.order.domain.application.service.domain.dto.message

import com.yunhalee.msa.architecture.common.domain.valueobject.PaymentStatus
import java.math.BigDecimal
import java.time.Instant

data class PaymentResponse (
    val id: String,
    val sagaId: String,
    val orderId: String,
    val paymentId: String,
    val customerId: String,
    val price: BigDecimal,
    val createdAt: Instant,
    val paymentStatus: PaymentStatus,
    val failureMessages: List<String>
) {

    constructor(builder: Builder) : this(
        builder.id,
        builder.sagaId,
        builder.orderId,
        builder.paymentId,
        builder.customerId,
        builder.price,
        builder.createdAt,
        builder.paymentStatus!!,
        builder.failureMessages
    )

    companion object {
        fun builder() = Builder()
    }

    class Builder {
        var id: String = ""
        var sagaId: String = ""
        var orderId: String = ""
        var paymentId: String = ""
        var customerId: String = ""
        var price: BigDecimal = BigDecimal.ZERO
        var createdAt: Instant = Instant.now()
        var paymentStatus: PaymentStatus? = null
        var failureMessages: List<String> = emptyList()

        fun id(id: String) = apply { this.id = id }
        fun sagaId(sagaId: String) = apply { this.sagaId = sagaId }
        fun orderId(orderId: String) = apply { this.orderId = orderId }
        fun paymentId(paymentId: String) = apply { this.paymentId = paymentId }
        fun customerId(customerId: String) = apply { this.customerId = customerId }
        fun price(price: BigDecimal) = apply { this.price = price }
        fun createdAt(createdAt: Instant) = apply { this.createdAt = createdAt }
        fun paymentStatus(paymentStatus: PaymentStatus) = apply { this.paymentStatus = paymentStatus }
        fun failureMessages(failureMessages: List<String>) = apply { this.failureMessages = failureMessages }

        fun build() = PaymentResponse(this)
    }


}

