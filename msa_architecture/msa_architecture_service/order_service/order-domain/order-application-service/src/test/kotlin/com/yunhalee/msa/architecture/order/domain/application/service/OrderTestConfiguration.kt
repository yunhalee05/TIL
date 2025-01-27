package com.yunhalee.msa.architecture.order.domain.application.service

import com.yunhalee.msa.architecture.order.domain.application.service.domain.ports.output.message.publisher.payment.OrderCancelPaymentRequestMessagePublisher
import com.yunhalee.msa.architecture.order.domain.application.service.domain.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher
import com.yunhalee.msa.architecture.order.domain.application.service.domain.ports.output.repository.CustomerRepository
import com.yunhalee.msa.architecture.order.domain.application.service.domain.ports.output.repository.OrderRepository
import com.yunhalee.msa.architecture.order.domain.application.service.domain.ports.output.repository.RestaurantRepository
import com.yunhalee.msa.architecture.service.order.domain.core.OrderDomainService
import com.yunhalee.msa.architecture.service.order.domain.core.OrderDomainServiceImpl
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication(scanBasePackages = ["com.yunhalee.msa.architecture.order.domain"])
class OrderTestConfiguration {
    @Bean
    fun orderCreatedPaymentRequestMessagePublisher(): OrderCreatedPaymentRequestMessagePublisher {
        return mock(OrderCreatedPaymentRequestMessagePublisher::class.java)
    }

    @Bean
    fun orderCancelPaymentRequestMessagePublisher(): OrderCancelPaymentRequestMessagePublisher {
        return mock(OrderCancelPaymentRequestMessagePublisher::class.java)
    }

    @Bean
    fun orderRepository(): OrderRepository {
        return mock(OrderRepository::class.java)
    }

    @Bean
    fun customerRepository(): CustomerRepository {
        return mock(CustomerRepository::class.java)
    }

    @Bean
    fun restaurantRepository(): RestaurantRepository {
        return mock(RestaurantRepository::class.java)
    }

    @Bean
    fun orderDomainService(): OrderDomainService {
        return OrderDomainServiceImpl()
    }
}

