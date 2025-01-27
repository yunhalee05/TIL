package com.yunhalee.msa.architecture.order.domain.application.service.domain.ports.input.message.listener.customer

import com.yunhalee.msa.architecture.order.domain.application.service.domain.dto.message.CustomerModel

interface CustomerMessageListener {

    fun customerCreated(customerModel: CustomerModel)

}