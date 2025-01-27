package com.yunhalee.msa.architecture.service.order.domain.core.entity

import com.yunhalee.msa.architecture.common.domain.entity.AggregateRoot
import com.yunhalee.msa.architecture.common.domain.valueobject.CustomerID
import com.yunhalee.msa.architecture.common.domain.valueobject.Money
import com.yunhalee.msa.architecture.common.domain.valueobject.OrderId
import com.yunhalee.msa.architecture.common.domain.valueobject.OrderStatus
import com.yunhalee.msa.architecture.common.domain.valueobject.RestaurantID
import com.yunhalee.msa.architecture.service.order.domain.core.exception.OrderDomainException
import com.yunhalee.msa.architecture.service.order.domain.core.valueobject.OrderItemID
import com.yunhalee.msa.architecture.service.order.domain.core.valueobject.StreetAddress
import com.yunhalee.msa.architecture.service.order.domain.core.valueobject.TrackingId
import java.util.UUID

class Order(
    val restaurantId: RestaurantID,
    val customerId: CustomerID,
    val streetAddress: StreetAddress,
    val price: Money,
    val orderItems: List<OrderItem>,

    var trackingId: TrackingId,
    var orderStatus: OrderStatus,
    var failureMessages: MutableList<String>
) : AggregateRoot<OrderId>() {

    private constructor(builder: Builder) : this(
        restaurantId = builder.restaurantId!!,
        customerId = builder.customerId!!,
        streetAddress = builder.streetAddress!!,
        price = builder.price!!,
        orderItems = builder.orderItems!!,
        trackingId = builder.trackingId!!,
        orderStatus = builder.orderStatus!!,
        failureMessages = builder.failureMessages!!
    ) {
        setId(builder.orderId!!)
    }

    companion object {
        fun builder(): Builder {
            return Builder()
        }
    }

    fun initializeOrder() {
        setId(OrderId(UUID.randomUUID()))
        trackingId = TrackingId(UUID.randomUUID())
        orderStatus = OrderStatus.PENDING
        initializeOrderItems()
    }

    private fun initializeOrderItems() {
        var itemId = 1L
        orderItems.forEach {
            it.initializeOrderItem(super.id!!, OrderItemID(itemId++))
        }
    }

    fun validateOrder() {
        validateInitialOrder()
        validateTotalPrice()
        validateItemsPrice()
    }

    private fun validateInitialOrder() {
        if (orderStatus != null || id != null) {
            throw OrderDomainException("Order is not in correct state for initialization!")
        }
    }

    private fun validateTotalPrice() {
        if (price == null || !price.isGreaterThanZero()) {
            throw OrderDomainException("Total price must be greater than zero!")
        }
    }

    private fun validateItemsPrice() {
        val orderItemsTotal: Money = orderItems.stream().map { orderItem: OrderItem ->
            validateItemPrice(orderItem)
            orderItem.subtotal
        }.reduce(Money.ZERO, Money::add)
        if (price != orderItemsTotal) {
            throw OrderDomainException((("Total price: " + price.amount) + " is not equal to Order items total: " + orderItemsTotal.amount) + "!")
        }
    }

    private fun validateItemPrice(orderItem: OrderItem) {
        if (!orderItem.isPriceValid()) {
            throw OrderDomainException(("Order item price: " + orderItem.price.amount) +
                " is not valid for product " + orderItem.product.id!!.getValue())
        }
    }

    fun pay() {
        if (orderStatus != OrderStatus.PENDING) {
            throw OrderDomainException("Order is not in correct state for pay operation!")
        }
        orderStatus = OrderStatus.PAID
    }

    fun approve() {
        if (orderStatus != OrderStatus.PAID) {
            throw OrderDomainException("Order is not in correct state for approve operation!")
        }
        orderStatus = OrderStatus.APPROVED
    }

    fun initCancel(failureMessages: List<String>?) {
        if (orderStatus != OrderStatus.PAID) {
            throw OrderDomainException("Order is not in correct state for initCancel operation!")
        }
        orderStatus = OrderStatus.CANCELLING
        updateFailureMessages(failureMessages)
    }

    fun cancel(failureMessages: List<String>) {
        if (!(orderStatus == OrderStatus.CANCELLING || orderStatus == OrderStatus.PENDING)) {
            throw OrderDomainException("Order is not in correct state for cancel operation!")
        }
        orderStatus = OrderStatus.CANCELED
        updateFailureMessages(failureMessages)
    }

    private fun updateFailureMessages(failureMessages: List<String>?) {
        if (failureMessages != null) {
            this.failureMessages.addAll(failureMessages.stream().filter { it != "" }.toList())
        }
    }

    class Builder(
        var orderId: OrderId? = null,
        var restaurantId: RestaurantID? = null,
        var customerId: CustomerID? = null,
        var streetAddress: StreetAddress? = null,
        var price: Money? = null,
        var orderItems: List<OrderItem>? = null,
        var trackingId: TrackingId? = null,
        var orderStatus: OrderStatus? = null,
        var failureMessages: MutableList<String>? = null
    ) {

        fun orderId(orderId: OrderId): Builder {
            this.orderId = orderId
            return this
        }

        fun restaurantId(restaurantId: RestaurantID): Builder {
            this.restaurantId = restaurantId
            return this
        }

        fun customerId(customerId: CustomerID): Builder {
            this.customerId = customerId
            return this
        }

        fun deliveryAddress(streetAddress: StreetAddress): Builder {
            this.streetAddress = streetAddress
            return this
        }

        fun price(price: Money): Builder {
            this.price = price
            return this
        }

        fun orderItems(orderItems: List<OrderItem>): Builder {
            this.orderItems = orderItems
            return this
        }

        fun trackingId(trackingId: TrackingId): Builder {
            this.trackingId = trackingId
            return this
        }

        fun orderStatus(orderStatus: OrderStatus): Builder {
            this.orderStatus = orderStatus
            return this
        }

        fun failureMessages(failureMessages: MutableList<String>): Builder {
            this.failureMessages = failureMessages
            return this
        }

        fun build(): Order {
            return Order(this)
        }
    }
}