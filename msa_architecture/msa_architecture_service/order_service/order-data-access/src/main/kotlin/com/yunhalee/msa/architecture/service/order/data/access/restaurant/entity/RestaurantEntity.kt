package com.yunhalee.msa.architecture.service.order.data.access.restaurant.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.Table
import java.math.BigDecimal
import java.util.UUID

@Entity
@Table(name = "order_restaurant_m_view", schema = "restaurant")
@IdClass(RestaurantEntityId::class)
class RestaurantEntity(
    @Id
    val restaurantId: UUID,

    @Id
    val productId: UUID,

    val restaurantName: String,

    val restaurantActive: Boolean,

    val productName: String,

    val productPrice: BigDecimal

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RestaurantEntity

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