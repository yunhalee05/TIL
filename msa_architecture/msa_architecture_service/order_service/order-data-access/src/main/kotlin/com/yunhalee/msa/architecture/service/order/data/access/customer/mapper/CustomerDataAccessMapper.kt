package com.yunhalee.msa.architecture.service.order.data.access.customer.mapper

import com.yunhalee.msa.architecture.common.domain.valueobject.CustomerID
import com.yunhalee.msa.architecture.service.order.data.access.customer.entity.CustomerEntity
import com.yunhalee.msa.architecture.service.order.domain.core.entity.Customer
import org.springframework.stereotype.Component

@Component
class CustomerDataAccessMapper {

    fun customerToCustomerEntity(customer: Customer): CustomerEntity {
        return CustomerEntity.builder()
            .id(customer.id!!.getValue())
            .build()
    }

    fun customerEntityToCustomer(customerEntity: CustomerEntity): Customer {
        return Customer.builder()
            .id(CustomerID(customerEntity.id))
            .build()
    }
}