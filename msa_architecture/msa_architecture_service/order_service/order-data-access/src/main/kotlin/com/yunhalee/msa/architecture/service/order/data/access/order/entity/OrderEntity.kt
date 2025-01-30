package com.yunhalee.msa.architecture.service.order.data.access.order.entity

import com.yunhalee.msa.architecture.common.domain.valueobject.OrderStatus
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.math.BigDecimal
import java.util.UUID

@Entity
@Table(name = "orders")
class OrderEntity(
    @Id
    val id: UUID,

    val customerId: UUID,

    val restaurantId: UUID,

    val trackingId: UUID,

    val price: BigDecimal,

    @Enumerated(EnumType.STRING)
    val status: OrderStatus,

    val failureMessages: String,

    @OneToOne(mappedBy = "order", cascade = [CascadeType.ALL])
    val address: OrderAddressEntity,

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL])
    val orderItems: List<OrderItemEntity>
) {

    constructor(builder: Builder) : this(
        id = builder.id!!,
        customerId = builder.customerId!!,
        restaurantId = builder.restaurantId!!,
        trackingId = builder.trackingId!!,
        price = builder.price!!,
        status = builder.status!!,
        failureMessages = builder.failureMessages!!,
        address = builder.address!!,
        orderItems = builder.orderItems!!
    )

    companion object {
        fun builder(): Builder {
            return Builder()
        }
    }

    class Builder {
        var id: UUID? = null
        var customerId: UUID? = null
        var restaurantId: UUID? = null
        var trackingId: UUID? = null
        var price: BigDecimal? = null
        var status: OrderStatus? = null
        var failureMessages: String? = null
        var address: OrderAddressEntity? = null
        var orderItems: List<OrderItemEntity>? = null

        fun id(id: UUID) = apply { this.id = id }
        fun customerId(customerId: UUID) = apply { this.customerId = customerId }
        fun restaurantId(restaurantId: UUID) = apply { this.restaurantId = restaurantId }
        fun trackingId(trackingId: UUID) = apply { this.trackingId = trackingId }
        fun price(price: BigDecimal) = apply { this.price = price }
        fun status(status: OrderStatus) = apply { this.status = status }
        fun failureMessages(failureMessages: String) = apply { this.failureMessages = failureMessages }
        fun address(address: OrderAddressEntity) = apply { this.address = address }
        fun orderItems(orderItems: List<OrderItemEntity>) = apply { this.orderItems = orderItems }

        fun build() = OrderEntity(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OrderEntity

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}