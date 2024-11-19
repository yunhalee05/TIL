package com.yunhalee.grpcclient.config.client

import com.google.common.collect.ImmutableMap
import net.devh.boot.grpc.client.autoconfigure.GrpcClientAutoConfiguration
import net.devh.boot.grpc.client.channelfactory.GrpcChannelFactory
import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator
import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.HealthIndicator
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy

@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(GrpcClientAutoConfiguration::class)
@ConditionalOnEnabledHealthIndicator("grpcChannel")
@ConditionalOnClass(name = ["org.springframework.boot.actuate.health.HealthIndicator"])
class GrpcClientHealthConfiguration {

    // 기존에 grpcChannel이 하나라도 TRANSIENT_FAILURE 상태일 경우 outOfService로 설정했던 것과 다르게, up인 상태에서 grpc channel의 state만 actuator 정보에 추가
    @Bean
    @Lazy
    fun grpcChannelHealthIndicator(factory: GrpcChannelFactory): HealthIndicator {
        return HealthIndicator {
            val states = ImmutableMap.copyOf(factory.connectivityState)
            val health: Health.Builder = Health.up()
            health.withDetails(states).build()
        }
    }
}
