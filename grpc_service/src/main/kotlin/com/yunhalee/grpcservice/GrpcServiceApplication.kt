package com.yunhalee.grpcservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GrpcServiceApplication

fun main(args: Array<String>) {
    runApplication<GrpcServiceApplication>(*args)
}
