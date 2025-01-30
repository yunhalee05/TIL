package com.yunhalee.msa.architecture.service.order.data.access.restaurant.adapter

import com.yunhalee.msa.architecture.order.domain.application.service.domain.ports.output.repository.RestaurantRepository
import com.yunhalee.msa.architecture.service.order.data.access.restaurant.mapper.RestaurantDataAccessMapper
import com.yunhalee.msa.architecture.service.order.data.access.restaurant.repository.RestaurantJpaRepository
import com.yunhalee.msa.architecture.service.order.domain.core.entity.Restaurant
import org.springframework.stereotype.Component
import java.util.Optional

@Component
class RestaurantRepositoryImpl(
    private val restaurantJpaRepository: RestaurantJpaRepository,
    private val restaurantDataAccessMapper: RestaurantDataAccessMapper
) : RestaurantRepository{
    override fun findRestaurantInformation(restaurant: Restaurant): Optional<Restaurant> {
        val restaurantProducts = restaurantDataAccessMapper.restaurantToRestaurantProduct(restaurant)
        if(restaurantProducts.isEmpty()){
            return Optional.empty()
        }
        return Optional.of(restaurantDataAccessMapper.restaurantEntityToRestaurant(restaurantJpaRepository.findByRestaurantIdAndProductIdIn(restaurant.id!!.getValue(), restaurantProducts)))
    }
}