//package com.yunhalee.grpcservice.config
//
//import net.devh.boot.grpc.common.autoconfigure.GrpcCommonCodecAutoConfiguration
//import net.devh.boot.grpc.common.autoconfigure.GrpcCommonTraceAutoConfiguration
//import net.devh.boot.grpc.server.autoconfigure.GrpcAdviceAutoConfiguration
//import net.devh.boot.grpc.server.autoconfigure.GrpcHealthServiceAutoConfiguration
//import net.devh.boot.grpc.server.autoconfigure.GrpcMetadataConsulConfiguration
//import net.devh.boot.grpc.server.autoconfigure.GrpcMetadataEurekaConfiguration
//import net.devh.boot.grpc.server.autoconfigure.GrpcMetadataNacosConfiguration
//import net.devh.boot.grpc.server.autoconfigure.GrpcMetadataZookeeperConfiguration
//import net.devh.boot.grpc.server.autoconfigure.GrpcReflectionServiceAutoConfiguration
//import net.devh.boot.grpc.server.autoconfigure.GrpcServerAutoConfiguration
//import net.devh.boot.grpc.server.autoconfigure.GrpcServerFactoryAutoConfiguration
//import net.devh.boot.grpc.server.autoconfigure.GrpcServerMetricAutoConfiguration
//import net.devh.boot.grpc.server.autoconfigure.GrpcServerSecurityAutoConfiguration
//import net.devh.boot.grpc.server.autoconfigure.GrpcServerTraceAutoConfiguration
//import org.springframework.boot.autoconfigure.ImportAutoConfiguration
//import org.springframework.context.annotation.Configuration
//
//
////https://velog.io/@mbsik6082/Spring-Boot-gRPC-Server-%EC%8B%A4%ED%96%89%EC%8B%9C%EC%9E%91-%EC%95%88-%EB%90%98%EB%8A%94-%EC%98%A4%EB%A5%98-gRPC-server-not-start-error-when-using-spring-boot
//@Configuration
//@ImportAutoConfiguration(GrpcCommonCodecAutoConfiguration::class, GrpcCommonTraceAutoConfiguration::class, GrpcAdviceAutoConfiguration::class, GrpcHealthServiceAutoConfiguration::class, GrpcMetadataConsulConfiguration::class, GrpcMetadataEurekaConfiguration::class, GrpcMetadataNacosConfiguration::class, GrpcMetadataZookeeperConfiguration::class, GrpcReflectionServiceAutoConfiguration::class, GrpcServerAutoConfiguration::class, GrpcServerFactoryAutoConfiguration::class, GrpcServerMetricAutoConfiguration::class, GrpcServerSecurityAutoConfiguration::class, GrpcServerTraceAutoConfiguration::class)
//class GrpcConfig