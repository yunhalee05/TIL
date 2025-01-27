package com.yunhalee.msa.architecture.order.domain.application.service.domain.ports.output.repository

import com.yunhalee.msa.architecture.service.order.domain.core.entity.Customer
import java.util.Optional
import java.util.UUID

interface CustomerRepository {

    fun findCustomer(customerId: UUID): Optional<Customer>

    fun save(customer: Customer): Customer
}