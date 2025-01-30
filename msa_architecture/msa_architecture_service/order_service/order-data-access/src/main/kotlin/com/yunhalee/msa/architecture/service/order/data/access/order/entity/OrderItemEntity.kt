package com.yunhalee.msa.architecture.service.order.data.access.order.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.math.BigDecimal
import java.util.UUID

@Entity
@Table(name = "order_item")
// multi column primary key를 설정할 수 있음
@IdClass(OrderItemEntityId::class)
class OrderItemEntity(
    @Id
    val id: Long,

    @Id
    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "order_id")
    var order: OrderEntity,

    val productId: UUID,

    val price: BigDecimal,

    val quantity: Int,

    val subtotal: BigDecimal

) {

    constructor(builder: Builder) : this(
        id = builder.id!!,
        order = builder.order!!,
        productId = builder.productId!!,
        price = builder.price!!,
        quantity = builder.quantity!!,
        subtotal = builder.subtotal!!
    )

    companion object {
        fun builder(): Builder {
            return Builder()
        }
    }

    class Builder {
        var id: Long? = null
        var order: OrderEntity? = null
        var productId: UUID? = null
        var price: BigDecimal? = null
        var quantity: Int? = null
        var subtotal: BigDecimal? = null

        fun id(id: Long) = apply { this.id = id }
        fun order(order: OrderEntity) = apply { this.order = order }
        fun productId(productId: UUID) = apply { this.productId = productId }
        fun price(price: BigDecimal) = apply { this.price = price }
        fun quantity(quantity: Int) = apply { this.quantity = quantity }
        fun subtotal(subtotal: BigDecimal) = apply { this.subtotal = subtotal }

        fun build() = OrderItemEntity(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OrderItemEntity

        if (id != other.id) return false
        if (order != other.order) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + order.hashCode()
        return result
    }
}
