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

class Customer(
    val username : String,
    val firstName : String,
    val lastName : String,
) : AggregateRoot<CustomerID>() {
    constructor(customerID: CustomerID): this("", "", ""){
        setId(customerID)
    }
}