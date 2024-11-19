package com.yunhalee.grpcclient

import net.devh.boot.grpc.client.autoconfigure.GrpcClientHealthAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/* Grpc Service의 응답 오류로 Grpc Client 서버자체가 죽어버리는 문제 해결을 위해 GrpcClientHealthAutoConfiguration 클래스를 제외한다. (https://yidongnan.github.io/grpc-spring-boot-starter/en/client/configuration.html 의 health indicator 참고)
    @Bean
    @Lazy
    public HealthIndicator grpcChannelHealthIndicator(final GrpcChannelFactory factory) {
        return () -> {
            final ImmutableMap<String, ConnectivityState> states = ImmutableMap.copyOf(factory.getConnectivityState());
            final Health.Builder health;
            if (states.containsValue(ConnectivityState.TRANSIENT_FAILURE)) {
                health = Health.outOfService();
            } else {
                health = Health.up();
            }
            return health.withDetails(states)
                .build();
        };
    }
 */
@SpringBootApplication(exclude = [GrpcClientHealthAutoConfiguration::class])
class GrpcClientApplication

fun main(args: Array<String>) {
    runApplication<GrpcClientApplication>(*args)
}
