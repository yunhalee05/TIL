package com.yunhalee.msa.architecture.service.order.data.access.customer.adapter

import com.yunhalee.msa.architecture.order.domain.application.service.domain.ports.output.repository.CustomerRepository
import com.yunhalee.msa.architecture.service.order.data.access.customer.mapper.CustomerDataAccessMapper
import com.yunhalee.msa.architecture.service.order.data.access.customer.repository.CustomerJpaRepository
import com.yunhalee.msa.architecture.service.order.domain.core.entity.Customer
import org.springframework.stereotype.Component
import java.util.Optional
import java.util.UUID

@Component
class CustomerRepositoryImpl(
    private val customerJpaRepository: CustomerJpaRepository,
    private val customerDataAccessMapper: CustomerDataAccessMapper
) : CustomerRepository{
    override fun findCustomer(customerId: UUID): Optional<Customer> {
        return customerJpaRepository.findById(customerId).map { customerDataAccessMapper.customerEntityToCustomer(it) }
    }

    override fun save(customer: Customer): Customer {
        return customerDataAccessMapper.customerEntityToCustomer(customerJpaRepository.save(customerDataAccessMapper.customerToCustomerEntity(customer)))
    }
}