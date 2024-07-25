package com.yunhalee.grpcclient.config.client

import com.yunhalee.grpcclient.config.GrpcClientHeaderInterceptor
import io.grpc.Channel
import io.grpc.ClientInterceptors
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class GrpcClientConfig {
    @Bean
    fun channel(
        @Value("\${grpc-client.host}")
        host: String,
        @Value("\${grpc-client.port}")
        port: Int
    ): Channel {
        val channel =
            NettyChannelBuilder
                .forAddress("localhost", 9090)
                .usePlaintext()
                .build()

        return ClientInterceptors.intercept(
            channel,
            mutableListOf(
                GrpcClientHeaderInterceptor(mapOf("Authorization" to "test")),
            ),
        )
    }
}