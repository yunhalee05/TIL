package com.yunhalee.msa.architecture.service.order.domain.core.entity

import com.yunhalee.msa.architecture.common.domain.entity.AggregateRoot
import com.yunhalee.msa.architecture.common.domain.valueobject.CustomerID
import com.yunhalee.msa.architecture.common.domain.valueobject.Money
import com.yunhalee.msa.architecture.common.domain.valueobject.OrderId
import com.yunhalee.msa.architecture.common.domain.valueobject.OrderStatus
import com.yunhalee.msa.architecture.common.domain.valueobject.RestaurantID
import com.yunhalee.msa.architecture.service.order.domain.core.exception.OrderDomainException
import com.yunhalee.msa.architecture.service.order.domain.core.valueobject.OrderItemID
import com.yunhalee.msa.architecture.service.order.domain.core.valueobject.StreetAddress
import com.yunhalee.msa.architecture.service.order.domain.core.valueobject.TrackingId
import java.util.UUID

class Restaurant(
    val products: List<Product>,
    var active: Boolean
) : AggregateRoot<RestaurantID>() {

    private constructor(builder: Builder) : this(
        products = builder.products!!,
        active = builder.active!!
    ) {
        setId(builder.restaurantId!!)
    }

    companion object {
        fun builder(): Builder {
            return Builder()
        }
    }

    class Builder {
        var restaurantId: RestaurantID? = null
        var products: List<Product>? = null
        var active: Boolean? = null

        fun restaurantId(restaurantId: RestaurantID) = apply { this.restaurantId = restaurantId }
        fun products(products: List<Product>) = apply { this.products = products }
        fun active(active: Boolean) = apply { this.active = active }

        fun build() = Restaurant(this)
    }
}