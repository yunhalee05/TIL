package com.example.springsecurity.config.security.grpc.reader

import com.example.springsecurity.config.security.authentication.token.AuthenticationHeaderToken
import com.example.springsecurity.config.security.authentication.token.HeaderToken
import io.grpc.Metadata
import io.grpc.ServerCall
import net.devh.boot.grpc.server.security.authentication.GrpcAuthenticationReader
import org.springframework.security.core.Authentication

class GrpcTokenReader : GrpcAuthenticationReader {
    override fun readAuthentication(call: ServerCall<*, *>, headers: Metadata): Authentication? {
        val accessToken = resolveAuthorizationToken(headers)
        val serviceTypeToken = resolveServiceTypeToken(headers)
        return if (accessToken != null || serviceTypeToken != null) {
            HeaderToken(authenticationToken = accessToken, serviceTypeToken = serviceTypeToken)
        } else {
            null
        }
    }

    private fun resolveAuthorizationToken(headers: Metadata): String? {
        val bearerToken = headers.get(Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER))
        if ((!bearerToken.isNullOrBlank()) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7)
        }
        return null
    }

    private fun resolveServiceTypeToken(headers: Metadata): String? {
        val serviceType = headers.get(Metadata.Key.of("Service-Type", Metadata.ASCII_STRING_MARSHALLER))
        if (serviceType.isNullOrBlank()) {
            return null
        }
        return serviceType
    }
}