package com.yunhalee.msa.architecture.service.order.container

import com.yunhalee.msa.architecture.service.order.domain.core.OrderDomainServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BeanConfiguration {

    @Bean
    fun orderDomainService() = OrderDomainServiceImpl()

}