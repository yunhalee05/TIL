package com.yunhalee.msa.architecture.service.order.domain.core.value

import java.util.UUID

data class StreetAddress(
    val id : UUID,
    val street: String,
    val postalCode: String,
    val city: String
) {
}