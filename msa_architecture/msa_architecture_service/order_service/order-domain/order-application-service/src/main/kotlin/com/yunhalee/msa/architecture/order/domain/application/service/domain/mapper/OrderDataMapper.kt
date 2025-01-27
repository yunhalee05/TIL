package com.yunhalee.msa.architecture.order.domain.application.service.domain.mapper

import com.yunhalee.msa.architecture.common.domain.valueobject.CustomerID
import com.yunhalee.msa.architecture.common.domain.valueobject.Money
import com.yunhalee.msa.architecture.common.domain.valueobject.OrderId
import com.yunhalee.msa.architecture.common.domain.valueobject.ProductID
import com.yunhalee.msa.architecture.common.domain.valueobject.RestaurantID
import com.yunhalee.msa.architecture.order.domain.application.service.domain.dto.create.CreateOrderCommand
import com.yunhalee.msa.architecture.order.domain.application.service.domain.dto.create.CreateOrderResponse
import com.yunhalee.msa.architecture.order.domain.application.service.domain.dto.create.OrderAddress
import com.yunhalee.msa.architecture.order.domain.application.service.domain.dto.create.OrderItem
import com.yunhalee.msa.architecture.order.domain.application.service.domain.dto.track.TrackOrderResponse
import com.yunhalee.msa.architecture.service.order.domain.core.entity.Order
import com.yunhalee.msa.architecture.service.order.domain.core.entity.Product
import com.yunhalee.msa.architecture.service.order.domain.core.entity.Restaurant
import com.yunhalee.msa.architecture.service.order.domain.core.valueobject.StreetAddress
import com.yunhalee.msa.architecture.service.order.domain.core.valueobject.TrackingId
import org.springframework.stereotype.Component
import java.util.UUID
import java.util.stream.Collectors

@Component
class OrderDataMapper {

    fun createOrderCommandToRestaurant(createOrderCommand: CreateOrderCommand): Restaurant {
        return Restaurant.builder()
            .restaurantId(RestaurantID(createOrderCommand.restaurantId))
            .products(createOrderCommand.items.stream().map { Product(ProductID(it.productId)) }.collect(Collectors.toList()))
            .build()
    }

    fun createOrderCommandToOrder(createOrderCommand: CreateOrderCommand): Order {
        return Order.builder()
            .customerId(CustomerID(createOrderCommand.customerId))
            .restaurantId(RestaurantID(createOrderCommand.restaurantId))
            .deliveryAddress(orderAddressToStreetAddress(createOrderCommand.address))
            .price(Money(createOrderCommand.price))
            .orderItems(createOrderItemEntities(createOrderCommand.items))
            .build()
    }


    fun orderToCreateOrderResponse(order: Order, message: String): CreateOrderResponse {
        return CreateOrderResponse.builder()
            .orderTrackingId(order.trackingId.getValue())
            .orderStatus(order.orderStatus)
            .message(message)
            .build()
    }

    fun orderToTrackOrderResponse(order: Order): TrackOrderResponse {
        return TrackOrderResponse.builder()
            .orderTrackingId(order.trackingId.getValue())
            .orderStatus(order.orderStatus)
            .build()
    }

    private fun orderAddressToStreetAddress(orderAddress: OrderAddress): StreetAddress {
        return StreetAddress(
            id = UUID.randomUUID(),
            street = orderAddress.street,
            city = orderAddress.city,
            postalCode = orderAddress.postalCode)
    }

    private fun createOrderItemEntities(orderItems: List<OrderItem>): List<com.yunhalee.msa.architecture.service.order.domain.core.entity.OrderItem> {
        return orderItems.stream().map {
            com.yunhalee.msa.architecture.service.order.domain.core.entity.OrderItem.builder()
                .product(Product(ProductID(it.productId)))
                .quantity(it.quantity)
                .price(Money(it.price))
                .subtotal(Money(it.subTotal))
                .build()
        }.collect(Collectors.toList())
    }


}