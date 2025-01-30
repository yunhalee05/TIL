package com.yunhalee.msa.architecture.service.order.data.access.customer.repository

import com.yunhalee.msa.architecture.service.order.data.access.customer.entity.CustomerEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CustomerJpaRepository: JpaRepository<CustomerEntity, UUID> {
}