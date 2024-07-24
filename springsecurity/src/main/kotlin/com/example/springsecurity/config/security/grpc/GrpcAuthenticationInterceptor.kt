package com.example.springsecurity.config.security.grpc

import com.example.springsecurity.exception.UnAuthenticatedException
import io.grpc.Context
import io.grpc.Metadata
import io.grpc.ServerCall
import io.grpc.ServerCallHandler
import net.devh.boot.grpc.server.security.authentication.GrpcAuthenticationReader
import net.devh.boot.grpc.server.security.interceptors.AbstractAuthenticatingServerCallListener
import net.devh.boot.grpc.server.security.interceptors.AuthenticatingServerInterceptor
import org.slf4j.LoggerFactory
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder


/*
    DefaultAuthenticatingServerInterceptor에서는 grpc Context를 리셋하고 있어 별도로 구현하였습니다.
    AuthenticatingServerCallListener을 사용하면 spring Security Context를 리셋하고 있기에, grpc Context를 이용해서 인증 정보를 가져올 수 있습니다.
    ex) AuthenticatingServerInterceptor.SECURITY_CONTEXT_KEY.get()
 */
class GrpcAuthenticatingInterceptor(
    private val grpcAuthenticationReader: GrpcAuthenticationReader,
    private val authenticationManager: AuthenticationManager,
) : AuthenticatingServerInterceptor {
    val logger = LoggerFactory.getLogger(GrpcAuthenticatingInterceptor::class.java)

    override fun <ReqT : Any, RespT : Any> interceptCall(
        call: ServerCall<ReqT, RespT>,
        headers: Metadata,
        next: ServerCallHandler<ReqT, RespT>,
    ): ServerCall.Listener<ReqT> {
        var authentication: Authentication?
        try {
            authentication = grpcAuthenticationReader.readAuthentication(call, headers)
        } catch (e: AuthenticationException) {
            logger.info("헤더 데이터를 읽어오는데 실패하였습니다. message: ${e.message}")
            throw e
        }
        if (authentication == null) {
            try {
                return next.startCall(call, headers)
            } catch (e: AccessDeniedException) {
                throw AccessDeniedException("접근 권한이 없습니다. " + e.message, e)
            }
        }
        if (authentication.details == null && authentication is AbstractAuthenticationToken) {
            authentication.details = call.attributes
        }
        logger.debug("인증이 확인되었습니다. : ${authentication.name}")

        authentication = authenticationManager.authenticate(authentication)
        val securityContext = SecurityContextHolder.createEmptyContext()
        securityContext.authentication = authentication
        SecurityContextHolder.setContext(securityContext)
        val grpcContext =
            Context.current().withValues(
                AuthenticatingServerInterceptor.SECURITY_CONTEXT_KEY, securityContext,
                AuthenticatingServerInterceptor.AUTHENTICATION_CONTEXT_KEY, authentication,
            )
       grpcContext.attach()

        logger.debug("인증에 성공하였습니다. 인증 정보 : {}, 인증 권한 : {}", authentication!!.name, authentication.authorities)

        return try {
            AuthenticatingServerCallListener<ReqT>(next.startCall(call, headers), grpcContext, securityContext)
        } catch (e: AccessDeniedException) {
            if (authentication is AnonymousAuthenticationToken) {
                logger.info(e.stackTraceToString())
                throw UnAuthenticatedException("인증되지 않은 사용자 입니다. " + e.message)
            } else {
                throw e
            }
        }
    }

    private class AuthenticatingServerCallListener<ReqT>(
        delegate: ServerCall.Listener<ReqT>,
        grpcContext: Context,
        private val securityContext: SecurityContext,
    ) : AbstractAuthenticatingServerCallListener<ReqT>(delegate, grpcContext) {
        override fun attachAuthenticationContext() {
            SecurityContextHolder.setContext(securityContext)
        }

        override fun detachAuthenticationContext() {
        }

        override fun onHalfClose() {
            try {
                super.onHalfClose()
            } catch (e: AccessDeniedException) {
                if (securityContext.authentication is AnonymousAuthenticationToken) {
                    throw RuntimeException("인증이 필요합니다. " + e.message, e)
                } else {
                    throw e
                }
            }
        }
    }
}