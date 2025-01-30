package com.yunhalee.msa.architecture.service.order.data.access.restaurant.entity

import java.io.Serializable
import java.util.UUID

class RestaurantEntityId(
    val restaurantId: UUID,
    val productId: UUID
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RestaurantEntityId

        if (restaurantId != other.restaurantId) return false
        if (productId != other.productId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = restaurantId.hashCode()
        result = 31 * result + productId.hashCode()
        return result
    }
}