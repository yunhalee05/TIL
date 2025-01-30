package com.yunhalee.msa.architecture.service.order.data.access.restaurant.repository

import com.yunhalee.msa.architecture.service.order.data.access.restaurant.entity.RestaurantEntity
import com.yunhalee.msa.architecture.service.order.data.access.restaurant.entity.RestaurantEntityId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface RestaurantJpaRepository: JpaRepository<RestaurantEntity, RestaurantEntityId>{

    fun findByRestaurantIdAndProductIdIn(restaurantId: UUID, productIds: List<UUID>): List<RestaurantEntity>
}