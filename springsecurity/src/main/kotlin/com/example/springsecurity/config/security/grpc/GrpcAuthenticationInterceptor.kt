package com.example.springsecurity.config.security.grpc

import com.example.springsecurity.config.security.authentication.MultiAuthentications
import com.example.springsecurity.config.security.authprovider.AuthenticationTokenAuthProvider
import com.example.springsecurity.config.security.authprovider.ServiceTokenAuthProvider
import com.example.springsecurity.exception.UnAuthenticatedException
import io.grpc.Contexts
import io.grpc.Metadata
import io.grpc.ServerCall
import io.grpc.ServerCallHandler
import net.devh.boot.grpc.server.security.interceptors.AuthenticatingServerInterceptor
import org.springframework.security.core.context.SecurityContextHolder
//
//class GrpcAuthenticationInterceptor(
//    private val authenticationTokenAuthProvider: AuthenticationTokenAuthProvider,
//    private val serviceTokenAuthProvider: ServiceTokenAuthProvider): AuthenticatingServerInterceptor {
//    override fun <ReqT : Any, RespT : Any> interceptCall(call: ServerCall<ReqT, RespT>, headers: Metadata, next: ServerCallHandler<ReqT, RespT>): ServerCall.Listener<ReqT> {
//        authenticate(headers)
//        return next.startCall(call, headers)
//    }
//
//
//    private fun authenticate(headers: Metadata) {
//        val accessToken = resolveAuthorizationToken(headers)
//        val serviceTypeToken = resolveServiceTypeToken(headers)
//        val authentication =
//            MultiAuthentications(
//                listOfNotNull(
//                    serviceTokenAuthProvider.authentication(serviceTypeToken),
//                    authenticationTokenAuthProvider.authentication(accessToken),
//                ).toMutableList(),
//            )
//        if (!authentication.isAuthenticated()) {
//            throw UnAuthenticatedException("인증되지 않은 사용자입니다.")
//        }
//        SecurityContextHolder.getContext().authentication = authentication
//    }
//
//    private fun resolveAuthorizationToken(headers: Metadata): String? {
//        val bearerToken = headers.get(Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER))
//        if ((!bearerToken.isNullOrBlank()) && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7)
//        }
//        return null
//    }
//
//    private fun resolveServiceTypeToken(headers: Metadata): String? {
//        val serviceType = headers.get(Metadata.Key.of("Service-Type", Metadata.ASCII_STRING_MARSHALLER))
//        if (serviceType.isNullOrBlank()) {
//            return null
//        }
//        return serviceType
//    }
//}