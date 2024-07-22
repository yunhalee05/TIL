package com.example.springsecurity.config.security.grpc.reader

import com.example.springsecurity.config.security.authentication.token.ServiceHeaderToken
import io.grpc.Metadata
import io.grpc.ServerCall
import net.devh.boot.grpc.server.security.authentication.GrpcAuthenticationReader
import org.springframework.security.core.Authentication

class GrpcServiceTokenReader: GrpcAuthenticationReader {
    override fun readAuthentication(call: ServerCall<*, *>, headers: Metadata): Authentication? {
        val serviceTypeToken = resolveServiceTypeToken(headers)
       return serviceTypeToken?.let{ServiceHeaderToken(serviceTypeToken)}
    }
    private fun resolveServiceTypeToken(headers: Metadata): String? {
        val serviceType = headers.get(Metadata.Key.of("Service-Type", Metadata.ASCII_STRING_MARSHALLER))
        if (serviceType.isNullOrBlank()) {
            return null
        }
        return serviceType
    }
}