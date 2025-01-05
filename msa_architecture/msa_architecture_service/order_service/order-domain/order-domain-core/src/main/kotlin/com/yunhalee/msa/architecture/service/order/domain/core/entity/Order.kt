package com.yunhalee.msa.architecture.service.order.domain.core.entity

import com.yunhalee.msa.architecture.common.domain.entity.AggregateRoot
import com.yunhalee.msa.architecture.common.domain.valueobject.CustomerID
import com.yunhalee.msa.architecture.common.domain.valueobject.Money
import com.yunhalee.msa.architecture.common.domain.valueobject.OrderId
import com.yunhalee.msa.architecture.common.domain.valueobject.RestaurantID
import com.yunhalee.msa.architecture.service.order.domain.core.value.StreetAddress
import java.util.UUID

class Order(
    val restaurantId: RestaurantID,
    val customerId: CustomerID,
    val streetAddress: StreetAddress,
    val price: Money,
    val orderItems : List<OrderItem>
) : AggregateRoot<OrderId>() {


}