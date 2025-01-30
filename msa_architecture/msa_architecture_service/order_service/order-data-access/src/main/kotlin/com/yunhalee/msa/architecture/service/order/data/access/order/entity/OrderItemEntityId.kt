package com.yunhalee.msa.architecture.service.order.data.access.order.entity

import java.io.Serializable

class OrderItemEntityId(
    val id: Long,
    val order: OrderEntity
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OrderItemEntityId

        if (id != other.id) return false
        if (order != other.order) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + order.hashCode()
        return result
    }
}