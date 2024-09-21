package com.yunhalee.grpcclient.config.client

import com.yunhalee.BookAuthorServiceGrpc
import com.yunhalee.grpcclient.config.GrpcClientHeaderInterceptor
import io.grpc.Channel
import io.grpc.ClientInterceptors
import io.grpc.ManagedChannel
import io.grpc.Metadata
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder
import io.grpc.stub.MetadataUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.net.URI


@Configuration
class GrpcClientConfig {
//    @Bean
//    fun channel(
//        @Value("\${grpc-client.host}")
//        host: String,
//        @Value("\${grpc-client.port}")
//        port: Int
//    ): Channel {
//        val channel =
//            NettyChannelBuilder
//                .forAddress("localhost", 9090)
//                .usePlaintext()
//                .build()
//
//        return ClientInterceptors.intercept(
//            channel,
//            mutableListOf(
//                GrpcClientHeaderInterceptor(mapOf("Authorization" to "test")),
//            ),
//        )
//    }

    @Bean
    fun channel(
        @Value("\${grpc.client.grpc-service.address}")
        address: String,
    ): Channel {
        val channel =
            NettyChannelBuilder
                .forAddress("localhost", 9090)
                .usePlaintext()
                .build()
        val uri = URI.create(address)
        val useTransportSecurity = uri.scheme.equals("grpc+https", true)
        val channelBuilder = NettyChannelBuilder.forAddress(uri.host, uri.port)
            .defaultLoadBalancingPolicy("round_robin")
        if (useTransportSecurity) {
            channelBuilder.useTransportSecurity()
        } else {
            channelBuilder.usePlaintext()
        }
        return ClientInterceptors.intercept(
            channel,
            mutableListOf(
                GrpcClientHeaderInterceptor(mapOf("Authorization" to "test")),
            ),
        )
    }

//
//    @Bean
//    fun managedChannel(
//        @Value("\${grpc.client.grpc-service.address}")
//        address: String,
//    ): ManagedChannel {
//        val uri = URI.create(address)
//        val useTransportSecurity = uri.scheme.equals("grpc+https", true)
//        val channelBuilder = NettyChannelBuilder.forAddress(uri.host, uri.port)
//            .defaultLoadBalancingPolicy("round_robin")
//        if (useTransportSecurity) {
//            channelBuilder.useTransportSecurity()
//        } else {
//            channelBuilder.usePlaintext()
//        }
//        return channelBuilder.build()
//    }
//
//    @Bean
//    fun metadata(): io.grpc.Metadata {
//        val metadata = Metadata()
//        val key = io.grpc.Metadata.Key.of("Authorization", io.grpc.Metadata.ASCII_STRING_MARSHALLER)
//        metadata.put(key, "Bearer token")
//        return metadata
//    }
//
//
//    @Bean
//    fun bookService(managedChannel: ManagedChannel, metadata: Metadata): BookAuthorServiceGrpc.BookAuthorServiceBlockingStub {
//        val stub = BookAuthorServiceGrpc.newBlockingStub(managedChannel)
//        stub.withInterceptors(MetadataUtils.newAttachHeadersInterceptor(metadata))
//        return stub
//    }


}