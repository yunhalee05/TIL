package com.yunhalee.msa.architecture.order.domain.application.service.domain

import com.yunhalee.msa.architecture.order.domain.application.service.domain.dto.message.RestaurantApprovalResponse
import com.yunhalee.msa.architecture.order.domain.application.service.domain.ports.input.message.listener.restaurantapproval.RestaurantApprovalMessageListener
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated

@Service
@Validated
class RestaurantApprovalMessageListenerImpl: RestaurantApprovalMessageListener {

    override fun orderApproved(restaurantApprovalResponse: RestaurantApprovalResponse) {
        TODO("Not yet implemented")
    }

    override fun orderRejected(restaurantApprovalResponse: RestaurantApprovalResponse) {
        TODO("Not yet implemented")
    }
}