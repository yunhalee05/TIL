package com.example.springsecurity.config.security.grpc.reader

import com.example.springsecurity.config.security.authentication.token.AuthenticationHeaderToken
import io.grpc.Metadata
import io.grpc.ServerCall
import net.devh.boot.grpc.server.security.authentication.GrpcAuthenticationReader
import org.springframework.security.core.Authentication

class GrpcAuthenticationTokenReader: GrpcAuthenticationReader {
    override fun readAuthentication(call: ServerCall<*, *>, headers: Metadata): Authentication? {
        val accessToken = resolveAuthorizationToken(headers)
       return accessToken?.let{ AuthenticationHeaderToken(accessToken)}
    }

    private fun resolveAuthorizationToken(headers: Metadata): String? {
        val bearerToken = headers.get(Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER))
        if ((!bearerToken.isNullOrBlank()) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7)
        }
        return null
    }
}