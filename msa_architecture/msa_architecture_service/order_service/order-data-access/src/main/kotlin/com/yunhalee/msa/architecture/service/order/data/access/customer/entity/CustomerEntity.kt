package com.yunhalee.msa.architecture.service.order.data.access.customer.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "order_customer_m_view", schema = "customer")
class CustomerEntity(
    @Id
    val id: UUID,
//    val username : String,
//    val firstName : String,
//    val lastName : String,
) {
    constructor(builder: Builder) : this(
        id = builder.id!!
    )

    companion object {
        fun builder(): Builder {
            return Builder()
        }
    }

    class Builder {
        var id: UUID? = null
        fun id(id: UUID) = apply { this.id = id }
        fun build() = CustomerEntity(this)
    }
}