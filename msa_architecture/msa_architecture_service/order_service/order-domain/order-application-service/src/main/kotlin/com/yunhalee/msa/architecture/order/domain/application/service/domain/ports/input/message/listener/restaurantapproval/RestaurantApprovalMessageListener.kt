package com.yunhalee.msa.architecture.order.domain.application.service.domain.ports.input.message.listener.restaurantapproval

import com.yunhalee.msa.architecture.order.domain.application.service.domain.dto.message.RestaurantApprovalResponse

interface RestaurantApprovalMessageListener {

    fun orderApproved(restaurantApprovalResponse: RestaurantApprovalResponse)

    fun orderRejected(restaurantApprovalResponse: RestaurantApprovalResponse)

}