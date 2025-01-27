package com.yunhalee.msa.architecture.service.order.domain.core.exception

import com.yunhalee.msa.architecture.common.domain.exception.DomainException

class OrderNotFoundException: DomainException {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}