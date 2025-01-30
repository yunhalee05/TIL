package com.yunhalee.msa.architecture.order.domain.application.service.domain.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@ConfigurationProperties(prefix = "order-service")
data class OrderServiceConfigData(
    val paymentRequestTopicName: String,
    val paymentResponseTopicName: String,
    val restaurantApprovalRequestTopicName: String,
    val restaurantApprovalResponseTopicName: String,
)