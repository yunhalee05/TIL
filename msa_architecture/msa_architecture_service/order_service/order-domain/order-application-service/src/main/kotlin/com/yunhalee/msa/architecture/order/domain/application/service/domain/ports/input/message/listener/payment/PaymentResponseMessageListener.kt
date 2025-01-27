package com.yunhalee.msa.architecture.order.domain.application.service.domain.ports.input.message.listener.payment

import com.yunhalee.msa.architecture.order.domain.application.service.domain.dto.message.PaymentResponse

interface PaymentResponseMessageListener {

    fun paymentCompleted(paymentResponse: PaymentResponse)

    fun paymentCancelled(paymentResponse: PaymentResponse)
}