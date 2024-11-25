package com.yunhalee.msa.architecture.common.domain

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CommonDomainApplication

fun main(args: Array<String>) {
    runApplication<CommonDomainApplication>(*args)
}
