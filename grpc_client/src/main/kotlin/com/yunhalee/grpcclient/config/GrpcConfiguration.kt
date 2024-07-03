package com.yunhalee.grpcclient.config

import io.grpc.ClientInterceptor
import net.devh.boot.grpc.client.interceptor.GrpcGlobalClientInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order

@Configuration
class GrpcConfiguration {

    @Order(1)
    @GrpcGlobalClientInterceptor
    fun grpcClientInterceptor(): ClientInterceptor {
        return GrpcClientInterceptor()
    }
}
