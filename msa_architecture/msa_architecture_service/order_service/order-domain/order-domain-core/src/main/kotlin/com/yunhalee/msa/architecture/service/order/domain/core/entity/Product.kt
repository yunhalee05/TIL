package com.yunhalee.msa.architecture.service.order.domain.core.entity

import com.yunhalee.msa.architecture.common.domain.entity.BaseEntity
import com.yunhalee.msa.architecture.common.domain.valueobject.Money
import com.yunhalee.msa.architecture.common.domain.valueobject.ProductID

class Product(
    val name: String,
    val price: Money
): BaseEntity<ProductID>() {
}