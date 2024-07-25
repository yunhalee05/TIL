package com.example.springsecurity.config

import com.example.springsecurity.config.security.Role
import com.example.springsecurity.config.security.authprovider.GrpcAuthProvider
//import com.example.springsecurity.config.security.grpc.GrpcAuthenticationInterceptor
import com.example.springsecurity.config.security.grpc.reader.GrpcTokenReader
import com.yunhalee.BookAuthorServiceGrpc
import net.devh.boot.grpc.server.security.authentication.CompositeGrpcAuthenticationReader
import net.devh.boot.grpc.server.security.authentication.GrpcAuthenticationReader
import net.devh.boot.grpc.server.security.check.AccessPredicate
import net.devh.boot.grpc.server.security.check.AccessPredicateVoter
import net.devh.boot.grpc.server.security.check.GrpcSecurityMetadataSource
import net.devh.boot.grpc.server.security.check.ManualGrpcSecurityMetadataSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.AccessDecisionManager
import org.springframework.security.access.AccessDecisionVoter
import org.springframework.security.access.vote.UnanimousBased
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager


@Configuration
class GrpcSecurityConfig {


//    @Order(InterceptorOrder.ORDER_SECURITY_AUTHENTICATION)
//    @GrpcGlobalServerInterceptor
//    fun authenticationInterceptor(
//        authenticationTokenAuthProvider: AuthenticationTokenAuthProvider,
//        serviceTokenAuthProvider: ServiceTokenAuthProvider
//    ): AuthenticatingServerInterceptor {
//        return GrpcAuthenticationInterceptor(authenticationTokenAuthProvider, serviceTokenAuthProvider)
//    }
//
//
//    @Order(InterceptorOrder.ORDER_SECURITY_AUTHORISATION)
//    @GrpcGlobalServerInterceptor
//    fun authorizationInterceptor(
//        authenticationTokenAuthProvider: AuthenticationTokenAuthProvider,
//        serviceTokenAuthProvider: ServiceTokenAuthProvider
//    ): AuthenticatingServerInterceptor {
//        return GrpcAuthenticationInterceptor(authenticationTokenAuthProvider, serviceTokenAuthProvider)
//    }

    // DefaultAuthenticatingServerInterceptor 의 우선순위보다 높게 줍니다. -> 안해도 됨
//    @Order(InterceptorOrder.ORDER_SECURITY_AUTHENTICATION - 100)
//    @GrpcGlobalServerInterceptor
//    fun authenticatingInterceptor(
//        grpcAuthenticationReader: GrpcAuthenticationReader,
//        authenticationManager: AuthenticationManager,
//    ): AuthenticatingServerInterceptor {
//        return GrpcAuthenticatingInterceptor(grpcAuthenticationReader, authenticationManager)
//    }

    @Bean
    fun authenticationManager(
        grpcAuthProvider: GrpcAuthProvider
    ): AuthenticationManager {
        val providers = listOf(
            grpcAuthProvider
        )
        return ProviderManager(providers)
    }

    @Bean
    fun authenticationReader(): GrpcAuthenticationReader {
        // 순차도 되는 듯 하다
//        return CompositeGrpcAuthenticationReader(listOf(GrpcAuthenticationTokenReader(), GrpcServiceTokenReader()))
// CompositeGrpcAuthenticationReader를 사용하면 순차적으로 인증 처리를 하기 때문에 앞선 리더에서 읽은 토큰이 인증 실패하면 (권한 체크 포함) 인증 자체가 실패해져버린다. 따라서 모두 포함하는 커스텀 리더를 만든다.
        return CompositeGrpcAuthenticationReader(listOf(GrpcTokenReader()))

    }

    @Bean
    fun grpcSecurityMetadataSource(): GrpcSecurityMetadataSource {
        val source = ManualGrpcSecurityMetadataSource()
        source.set(BookAuthorServiceGrpc.getGetAuthorMethod(), AccessPredicate.hasAnyRole(Role.of(Role.SERVICE)))
//        AuthorizationCheckingServerInterceptor
//        source.set(BookAuthorServiceGrpc.getGetAuthorMethod(), AccessPredicate.hasAnyRole(Role.MEMBER, Role.SERVICE))
        source.setDefault(AccessPredicate.permitAll())
        return source
    }

    @Bean
    fun accessDecisionManager(): AccessDecisionManager {
        val voters: MutableList<AccessDecisionVoter<*>> = ArrayList()
        voters.add(AccessPredicateVoter())
        return UnanimousBased(voters)
    }
}