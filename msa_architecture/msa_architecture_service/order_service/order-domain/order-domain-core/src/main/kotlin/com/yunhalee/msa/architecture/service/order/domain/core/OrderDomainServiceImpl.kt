package com.yunhalee.msa.architecture.service.order.domain.core

import com.yunhalee.msa.architecture.service.order.domain.core.entity.Order
import com.yunhalee.msa.architecture.service.order.domain.core.entity.Product
import com.yunhalee.msa.architecture.service.order.domain.core.entity.Restaurant
import com.yunhalee.msa.architecture.service.order.domain.core.event.OrderCanceledEvent
import com.yunhalee.msa.architecture.service.order.domain.core.event.OrderCreateEvent
import com.yunhalee.msa.architecture.service.order.domain.core.event.OrderPaidEvent
import com.yunhalee.msa.architecture.service.order.domain.core.exception.OrderDomainException
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.logging.Logger

class OrderDomainServiceImpl : OrderDomainService {

    private val logger = Logger.getLogger(OrderDomainServiceImpl::class.java.name)

    override fun validateAndInitializeOrder(order: Order, restaurant: Restaurant): OrderCreateEvent {
        validateRestaurant(restaurant)
        setOrderProductInformation(order, restaurant)
        order.validateOrder()
        order.initializeOrder()
        logger.info("Order with id: ${order.id!!.getValue()} is initiated")
        return OrderCreateEvent(order, ZonedDateTime.now(ZoneId.of("UTC")))
    }


    override fun payOrder(order: Order): OrderPaidEvent {
        order.pay()
        logger.info("Order with id: ${order.id!!.getValue()} is paid", )
        return OrderPaidEvent(order, ZonedDateTime.now(ZoneId.of("UTC")))
    }

    override fun approveOrder(order: Order) {
        order.approve()
        logger.info("Order with id: ${order.id!!.getValue()} is approved")
    }

    override fun cancelOrderPayment(order: Order, failureMessages: List<String>): OrderCanceledEvent {
        order.initCancel(failureMessages)
        logger.info("Order payment is cancelling for order id: ${order.id!!.getValue()}")
        return OrderCanceledEvent(order, ZonedDateTime.now(ZoneId.of("UTC")))
    }

    override fun cancelOrder(order: Order, failureMessages: List<String>) {
        order.cancel(failureMessages)
        logger.info("Order with id: ${order.id!!.getValue()} is cancelled")
    }

    private fun validateRestaurant(restaurant: Restaurant) {
        if (restaurant.active.not()) {
            throw OrderDomainException(("Restaurant with id " + restaurant.id!!.getValue()) + " is currently not active!")
        }
    }

    private fun setOrderProductInformation(order: Order, restaurant: Restaurant) {
        order.orderItems.forEach { orderItem ->
            restaurant.products.forEach { restaurantProduct ->
                val currentProduct: Product = orderItem.product
                if (currentProduct == restaurantProduct) {
                    currentProduct.updateWithConfirmedNameAndPrice(restaurantProduct.name, restaurantProduct.price)
                }
            }
        }
    }
}