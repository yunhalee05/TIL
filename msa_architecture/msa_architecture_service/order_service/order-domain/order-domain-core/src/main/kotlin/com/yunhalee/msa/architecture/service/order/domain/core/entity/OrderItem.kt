package com.yunhalee.msa.architecture.service.order.domain.core.entity

import com.yunhalee.msa.architecture.common.domain.entity.BaseEntity
import com.yunhalee.msa.architecture.common.domain.valueobject.Money
import com.yunhalee.msa.architecture.common.domain.valueobject.OrderId
import com.yunhalee.msa.architecture.service.order.domain.core.valueobject.OrderItemID

class OrderItem(
    var orderId: OrderId? = null,
    val product: Product,
    val quantity: Int,
    val price: Money,
    val subtotal: Money
) : BaseEntity<OrderItemID>() {

    private constructor(builder: Builder) : this(
        product = builder.product!!,
        quantity = builder.quantity,
        price = builder.price!!,
        subtotal = builder.subtotal!!
    ) {
        setId(builder.orderItemId!!)
    }

    companion object {
        fun builder(): Builder {
            return Builder()
        }
    }

    fun initializeOrderItem(orderId: OrderId, orderItemId: OrderItemID) {
        this.orderId = orderId
        setId(orderItemId)
    }

    fun isPriceValid(): Boolean {
        return price.isGreaterThanZero() &&
            price == product.price &&
            price.multiply(quantity) == subtotal
    }

    class Builder(
        var orderItemId: OrderItemID? = null,
        var product: Product? = null,
        var quantity: Int = 0,
        var price: Money? = null,
        var subtotal: Money? = null,
    ) {

        fun orderItemId(id: OrderItemID): Builder {
            orderItemId = id
            return this
        }

        fun product(product: Product?): Builder {
            this.product = product
            return this
        }

        fun quantity(quantity: Int): Builder {
            this.quantity = quantity
            return this
        }

        fun price(money: Money?): Builder {
            this.price = money
            return this
        }

        fun subtotal(subtotal: Money): Builder {
            this.subtotal = subtotal
            return this
        }

        fun build(): OrderItem {
            return OrderItem(this)
        }

    }

}