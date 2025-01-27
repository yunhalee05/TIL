package com.yunhalee.msa.architecture.order.domain.application.service.domain

import com.yunhalee.msa.architecture.order.domain.application.service.domain.dto.create.CreateOrderCommand
import com.yunhalee.msa.architecture.order.domain.application.service.domain.dto.create.CreateOrderResponse
import com.yunhalee.msa.architecture.order.domain.application.service.domain.mapper.OrderDataMapper
import com.yunhalee.msa.architecture.order.domain.application.service.domain.ports.output.repository.CustomerRepository
import com.yunhalee.msa.architecture.order.domain.application.service.domain.ports.output.repository.OrderRepository
import com.yunhalee.msa.architecture.order.domain.application.service.domain.ports.output.repository.RestaurantRepository
import com.yunhalee.msa.architecture.service.order.domain.core.OrderDomainService
import com.yunhalee.msa.architecture.service.order.domain.core.entity.Order
import com.yunhalee.msa.architecture.service.order.domain.core.entity.Restaurant
import com.yunhalee.msa.architecture.service.order.domain.core.exception.OrderDomainException
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import java.util.logging.Logger

@Component
class OrderCreateCommandHandler(
    private val orderDomainService: OrderDomainService,
    private val orderRepository: OrderRepository,
    private val customerRepository: CustomerRepository,
    private val restaurantRepository: RestaurantRepository,
    private val orderDataMapper: OrderDataMapper
) {

    private val logger = Logger.getLogger(OrderCreateCommandHandler::class.java.name)


    @Transactional
    fun createOrder(createOrderCommand: CreateOrderCommand): CreateOrderResponse {
        checkCustomer(createOrderCommand.customerId)
        val restaurant = checkRestaurant(createOrderCommand)
        val order = orderDataMapper.createOrderCommandToOrder(createOrderCommand)
        val orderCreateEvent = orderDomainService.validateAndInitializeOrder(order, restaurant)
        val orderResult = saveOrder(order)
        logger.info("Order is created with id : ${orderResult.id}")
        return orderDataMapper.orderToCreateOrderResponse(orderResult)
    }

    private fun checkCustomer(customerId: UUID) {
        customerRepository.findCustomer(customerId).orElseThrow {
            logger.warning("Customer not found. id : $customerId")
            throw OrderDomainException("Customer not found. id : $customerId")
        }
    }

    private fun checkRestaurant(createOrderCommand: CreateOrderCommand): Restaurant {
        val restaurant = orderDataMapper.createOrderCommandToRestaurant(createOrderCommand)
        return restaurantRepository.findRestaurantInformation(restaurant).orElseThrow {
            logger.warning("Restaurant not found. id : ${restaurant.id}")
            throw OrderDomainException("Restaurant not found. id : ${restaurant.id}")
        }
    }

    private fun saveOrder(order: Order): Order {
        val order = orderRepository.save(order)
        logger.info("Order is saved with id : ${order.id}")
        return order
    }

}