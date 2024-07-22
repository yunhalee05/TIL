package com.example.springsecurity.config.security

import com.example.springsecurity.config.security.authprovider.AuthenticationTokenAuthProvider
import com.example.springsecurity.config.security.authprovider.ServiceTokenAuthProvider
//import com.example.springsecurity.config.security.grpc.GrpcAuthenticationInterceptor
import com.example.springsecurity.config.security.grpc.reader.GrpcAuthenticationTokenReader
import com.example.springsecurity.config.security.grpc.reader.GrpcServiceTokenReader
import com.yunhalee.BookAuthorServiceGrpc
import net.devh.boot.grpc.common.util.InterceptorOrder
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor
import net.devh.boot.grpc.server.security.authentication.CompositeGrpcAuthenticationReader
import net.devh.boot.grpc.server.security.authentication.GrpcAuthenticationReader
import net.devh.boot.grpc.server.security.check.AccessPredicate
import net.devh.boot.grpc.server.security.check.AccessPredicateVoter
import net.devh.boot.grpc.server.security.check.GrpcSecurityMetadataSource
import net.devh.boot.grpc.server.security.check.ManualGrpcSecurityMetadataSource
import net.devh.boot.grpc.server.security.interceptors.AuthenticatingServerInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.access.AccessDecisionManager
import org.springframework.security.access.AccessDecisionVoter
import org.springframework.security.access.vote.UnanimousBased
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authorization.AuthorizationManager
import org.springframework.security.authorization.AuthorizationManagers


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

    @Bean
    fun authenticationManager(
        authenticationTokenAuthProvider: AuthenticationTokenAuthProvider,
        serviceTokenAuthProvider: ServiceTokenAuthProvider
    ): AuthenticationManager {
        val providers = mutableListOf(
            authenticationTokenAuthProvider,
            serviceTokenAuthProvider
        )
        return ProviderManager(providers)
    }

    @Bean
    fun authenticationReader(): GrpcAuthenticationReader {
        return CompositeGrpcAuthenticationReader(listOf(GrpcAuthenticationTokenReader(), GrpcServiceTokenReader()))
    }

    @Bean
    fun grpcSecurityMetadataSource(): GrpcSecurityMetadataSource {
        val source = ManualGrpcSecurityMetadataSource()
        source.set(BookAuthorServiceGrpc.getGetAuthorMethod(), AccessPredicate.hasAnyRole(Role.SERVICE))
//        source.set(BookAuthorServiceGrpc.getGetAuthorMethod(), AccessPredicate.hasAnyRole(Role.MEMBER))
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