package com.yunhalee.msa.architecture.order.domain.application.service.domain

import com.yunhalee.msa.architecture.order.domain.application.service.domain.dto.message.PaymentResponse
import com.yunhalee.msa.architecture.order.domain.application.service.domain.ports.input.message.listener.payment.PaymentResponseMessageListener
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated

@Service
@Validated
class PaymentResponseMessageListenerImpl : PaymentResponseMessageListener {

    override fun paymentCompleted(paymentResponse: PaymentResponse) {
        TODO("Not yet implemented")
    }

    override fun paymentCancelled(paymentResponse: PaymentResponse) {
        TODO("Not yet implemented")
    }
}