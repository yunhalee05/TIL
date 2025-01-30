package com.yunhalee.msa.architecture.service.order.data.access.order.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "order_address")
class OrderAddressEntity(

    @Id
    val id: UUID,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "order_id")
    var order: OrderEntity,

    val street: String,

    val postalCode: String,

    val city: String
) {
    constructor(builder: Builder) : this(
        id = builder.id!!,
        order = builder.order!!,
        street = builder.street!!,
        postalCode = builder.postalCode!!,
        city = builder.city!!
    )

    companion object {
        fun builder(): Builder {
            return Builder()
        }
    }

    class Builder {
        var id: UUID? = null
        var order: OrderEntity? = null
        var street: String? = null
        var postalCode: String? = null
        var city: String? = null

        fun id(id: UUID) = apply { this.id = id }
        fun order(order: OrderEntity) = apply { this.order = order }
        fun street(street: String) = apply { this.street = street }
        fun postalCode(postalCode: String) = apply { this.postalCode = postalCode }
        fun city(city: String) = apply { this.city = city }

        fun build() = OrderAddressEntity(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OrderAddressEntity

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
