package com.yunhalee.msa.architecture.service.order.domain.core.entity

import com.yunhalee.msa.architecture.common.domain.entity.BaseEntity
import com.yunhalee.msa.architecture.common.domain.valueobject.Money
import com.yunhalee.msa.architecture.common.domain.valueobject.ProductID

class Product(
    var name: String,
    var price: Money
) : BaseEntity<ProductID>() {

    constructor(id: ProductID) : this("", Money.ZERO) {
        setId(id)
    }

    constructor(id: ProductID, name: String, price: Money) : this(name, price) {
        setId(id)
    }

    fun updateWithConfirmedNameAndPrice(name: String, price: Money) {
        this.name = name
        this.price = price
    }

}