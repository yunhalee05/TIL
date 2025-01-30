package com.yunhalee.msa.architecture.service.order.data.access.order.mapper

import com.yunhalee.msa.architecture.common.domain.valueobject.CustomerID
import com.yunhalee.msa.architecture.common.domain.valueobject.Money
import com.yunhalee.msa.architecture.common.domain.valueobject.OrderId
import com.yunhalee.msa.architecture.common.domain.valueobject.ProductID
import com.yunhalee.msa.architecture.common.domain.valueobject.RestaurantID
import com.yunhalee.msa.architecture.service.order.data.access.order.entity.OrderAddressEntity
import com.yunhalee.msa.architecture.service.order.data.access.order.entity.OrderEntity
import com.yunhalee.msa.architecture.service.order.data.access.order.entity.OrderItemEntity
import com.yunhalee.msa.architecture.service.order.domain.core.entity.Order
import com.yunhalee.msa.architecture.service.order.domain.core.entity.Order.Companion.FAILURE_MESSAGE_DELIMITER
import com.yunhalee.msa.architecture.service.order.domain.core.entity.OrderItem
import com.yunhalee.msa.architecture.service.order.domain.core.entity.Product
import com.yunhalee.msa.architecture.service.order.domain.core.valueobject.OrderItemID
import com.yunhalee.msa.architecture.service.order.domain.core.valueobject.StreetAddress
import com.yunhalee.msa.architecture.service.order.domain.core.valueobject.TrackingId
import org.springframework.stereotype.Component

@Component
class OrderDataAccessMapper {

    fun orderToOrderEntity(order: Order): OrderEntity {
        val orderEntity = OrderEntity.builder()
            .id(order.id!!.getValue())
            .customerId(order.customerId.getValue())
            .restaurantId(order.restaurantId.getValue())
            .trackingId(order.trackingId.getValue())
            .address(deliveryAddressToAddressEntity(order.streetAddress))
            .price(order.price.amount)
            .orderItems(orderItemsToOrderItemEntities(order.orderItems))
            .status(order.orderStatus)
            .failureMessages(order.failureMessages.joinToString(FAILURE_MESSAGE_DELIMITER))
            .build()
        orderEntity.address.order = orderEntity
        orderEntity.orderItems.forEach { it.order = orderEntity }
        return orderEntity
    }

    private fun deliveryAddressToAddressEntity(streetAddress: StreetAddress): OrderAddressEntity {
        return OrderAddressEntity.builder()
            .street(streetAddress.street)
            .city(streetAddress.city)
            .postalCode(streetAddress.postalCode)
            .build()
    }

    private fun orderItemsToOrderItemEntities(orderItems: List<OrderItem>): List<OrderItemEntity> {
        return orderItems.map {
            OrderItemEntity.builder()
                .id(it.id!!.getValue())
                .productId(it.product.id!!.getValue())
                .price(it.price.amount)
                .quantity(it.quantity)
                .subtotal(it.subtotal.amount)
                .build()
        }
    }

    fun orderEntityToOrder(orderEntity: OrderEntity): Order {
        return Order.builder()
            .orderId(OrderId(orderEntity.id))
            .customerId(CustomerID(orderEntity.customerId))
            .restaurantId(RestaurantID(orderEntity.restaurantId))
            .deliveryAddress(addressEntityToDeliveryAddress(orderEntity.address))
            .price(Money(orderEntity.price))
            .orderItems(orderItemEntitiesToOrderItems(orderEntity.orderItems))
            .trackingId(TrackingId(orderEntity.trackingId))
            .orderStatus(orderEntity.status)
            .failureMessages(orderEntity.failureMessages.split(FAILURE_MESSAGE_DELIMITER).toMutableList())
            .build()
    }

    private fun addressEntityToDeliveryAddress(addressEntity: OrderAddressEntity): StreetAddress {
        return StreetAddress(
            id = addressEntity.id,
            street = addressEntity.street,
            city = addressEntity.city,
            postalCode = addressEntity.postalCode
        )
    }

    private fun orderItemEntitiesToOrderItems(orderItemEntities: List<OrderItemEntity>): List<OrderItem> {
        return orderItemEntities.map {
            OrderItem.builder()
                .orderItemId(OrderItemID(it.id))
                .product(Product(ProductID(it.productId)))
                .quantity(it.quantity)
                .price(Money(it.price))
                .subtotal(Money(it.subtotal))
                .build()
        }
    }
}