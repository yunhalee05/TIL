package com.yunhalee.msa.architecture.service.order.domain.core.entity

import com.yunhalee.msa.architecture.common.domain.entity.BaseEntity
import com.yunhalee.msa.architecture.common.domain.valueobject.Money
import com.yunhalee.msa.architecture.common.domain.valueobject.OrderId
import com.yunhalee.msa.architecture.service.order.domain.core.value.OrderItemID

class OrderItem(
    var orderId: OrderId,
    val product: Product,
    val quantity: Int,
    val price: Money,
    val subtotal: Money
): BaseEntity<OrderItemID>() {

}