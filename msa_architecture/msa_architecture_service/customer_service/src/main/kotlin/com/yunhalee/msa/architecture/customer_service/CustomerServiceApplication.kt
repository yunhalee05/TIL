package com.yunhalee.msa.architecture.customer_service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.yunhalee.msa.architecture"])
class CustomerServiceApplication

fun main(args: Array<String>) {
    runApplication<CustomerServiceApplication>(*args)
}
