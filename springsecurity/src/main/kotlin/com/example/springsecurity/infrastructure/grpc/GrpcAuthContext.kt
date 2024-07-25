package com.example.springsecurity.infrastructure.grpc

import com.example.springsecurity.config.security.authentication.MultiAuthentications
import com.example.springsecurity.config.security.authentication.TokenMember
import com.example.springsecurity.config.security.authentication.TokenService
import net.devh.boot.grpc.server.security.interceptors.AuthenticatingServerInterceptor

class GrpcAuthContext {

    companion object {
        private fun authentication() = AuthenticatingServerInterceptor.AUTHENTICATION_CONTEXT_KEY.get() as MultiAuthentications

        fun getMember() : TokenMember? {
            authentication().authentications.forEach { principal ->
                if (principal.tokenUser is TokenMember) {
                    return principal.tokenUser
                }
            }
            return null
        }

        fun getService(): TokenService? {
            authentication().authentications.forEach { principal ->
                if (principal.tokenUser is TokenService) {
                    return principal.tokenUser
                }
            }
            return null
        }
    }

}