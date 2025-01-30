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
) : AggregateRoot<CustomerID>() {
    constructor(customerID: CustomerID): this(){
        setId(customerID)
    }

    constructor(builder: Builder): this(){
        setId(builder.id!!)
    }

    companion object {
        fun builder(): Builder {
            return Builder()
        }
    }

    class Builder {
        var id: CustomerID? = null
        fun id(id: CustomerID) = apply { this.id = id }
        fun build() = Customer(this)
    }
}