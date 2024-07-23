package com.example.springsecurity.config.security.authprovider

import com.example.springsecurity.config.security.authentication.MultiAuthentications
import com.example.springsecurity.config.security.authentication.token.HeaderToken
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class GrpcAuthProvider(
    private val authenticationTokenProvider: AuthenticationTokenAuthProvider,
    private val serviceTokenProvider: ServiceTokenAuthProvider
): AuthenticationProvider {
    override fun authenticate(authentication: Authentication?): Authentication {
        return MultiAuthentications(
            listOfNotNull(
                serviceTokenProvider.authenticate(authentication),
                authenticationTokenProvider.authenticate(authentication)
            ).toMutableList()
        )

    }

    override fun supports(authentication: Class<*>?): Boolean {
    return authentication == HeaderToken::class.java
    }
}