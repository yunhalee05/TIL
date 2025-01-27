package com.yunhalee.msa.architecture.service.order.domain.core

import com.yunhalee.msa.architecture.service.order.domain.core.entity.Order
import com.yunhalee.msa.architecture.service.order.domain.core.entity.Restaurant
import com.yunhalee.msa.architecture.service.order.domain.core.event.OrderCanceledEvent
import com.yunhalee.msa.architecture.service.order.domain.core.event.OrderCreateEvent
import com.yunhalee.msa.architecture.service.order.domain.core.event.OrderPaidEvent

interface OrderDomainService {

    fun validateAndInitializeOrder(order: Order, restaurant: Restaurant): OrderCreateEvent

    fun payOrder(order: Order): OrderPaidEvent

    fun approveOrder(order: Order)

    fun cancelOrderPayment(order: Order, failureMessages: List<String>): OrderCanceledEvent

    fun cancelOrder(order: Order, failureMessages: List<String>)
}