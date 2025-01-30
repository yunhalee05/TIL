package com.yunhalee.msa.architecture.service.order.data.access.restaurant.mapper

import com.yunhalee.msa.architecture.common.domain.valueobject.Money
import com.yunhalee.msa.architecture.common.domain.valueobject.ProductID
import com.yunhalee.msa.architecture.common.domain.valueobject.RestaurantID
import com.yunhalee.msa.architecture.service.order.data.access.restaurant.entity.RestaurantEntity
import com.yunhalee.msa.architecture.service.order.data.access.restaurant.exception.RestaurantDataAccessException
import com.yunhalee.msa.architecture.service.order.domain.core.entity.Product
import com.yunhalee.msa.architecture.service.order.domain.core.entity.Restaurant
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.util.UUID

@Component
class RestaurantDataAccessMapper {

    fun restaurantToRestaurantProduct(restaurant: Restaurant): List<UUID> {
        return restaurant.products.map { it.id!!.getValue() }
    }

    fun restaurantEntityToRestaurant(restaurantEntities: List<RestaurantEntity>): Restaurant {
        val restaurantEntity = restaurantEntities.firstOrNull() ?: throw RestaurantDataAccessException("Restaurant not found")

        val products = restaurantEntities.map {
            Product(
                ProductID(it.productId),
                it.productName,
                Money(it.productPrice)
            )
        }
        return Restaurant.builder()
            .restaurantId(RestaurantID(restaurantEntity.restaurantId))
            .products(products)
            .active(restaurantEntity.restaurantActive)
            .build()
    }
}