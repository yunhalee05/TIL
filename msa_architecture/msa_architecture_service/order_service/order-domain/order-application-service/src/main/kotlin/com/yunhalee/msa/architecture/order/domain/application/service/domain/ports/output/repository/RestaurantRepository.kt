package com.yunhalee.msa.architecture.order.domain.application.service.domain.ports.output.repository

import com.yunhalee.msa.architecture.service.order.domain.core.entity.Restaurant
import java.util.Optional


interface RestaurantRepository {
    fun findRestaurantInformation(restaurant: Restaurant): Optional<Restaurant>

}