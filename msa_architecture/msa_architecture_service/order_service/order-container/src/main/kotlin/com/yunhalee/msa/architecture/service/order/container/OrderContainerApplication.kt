package com.yunhalee.msa.architecture.service.order.container

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EnableJpaRepositories(basePackages = ["com.yunhalee.msa.order.service.data.access"])
@EntityScan(basePackages = ["com.yunhalee.msa.order.service.data.access"])
@SpringBootApplication(scanBasePackages = ["com.yunhalee.msa.architecture"])
class OrderContainerApplication

fun main(args: Array<String>) {
	runApplication<OrderContainerApplication>(*args)
}
