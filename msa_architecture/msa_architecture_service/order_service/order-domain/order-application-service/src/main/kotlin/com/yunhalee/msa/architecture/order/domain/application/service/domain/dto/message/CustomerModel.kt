package com.yunhalee.msa.architecture.order.domain.application.service.domain.dto.message

import com.yunhalee.msa.architecture.common.domain.valueobject.PaymentStatus
import java.math.BigDecimal
import java.time.Instant

data class CustomerModel (
    val id: String,
    val username: String,
    val firstname: String,
    val lastname: String,
)

