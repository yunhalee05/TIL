package com.example.springsecurity.config.security.authprovider

import com.example.springsecurity.config.security.Role
import com.example.springsecurity.config.security.authentication.token.ServiceHeaderToken
import com.example.springsecurity.config.security.authentication.TokenAuthentication
import com.example.springsecurity.config.security.authentication.TokenService
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component

@Component
class ServiceTokenAuthProvider: AuthenticationProvider {

    private val serviceToken = "SERVICE"
    override fun authenticate(authentication: Authentication): TokenAuthentication? {
        val serviceToken = authentication.credentials as String?
        if (serviceToken.isNullOrEmpty()) {
            return null
        }
        return TokenAuthentication(convertMemberDetails(serviceToken))
    }

    override fun supports(authentication: Class<*>): Boolean {
        return authentication == ServiceHeaderToken::class.java
    }

    private fun convertMemberDetails(serviceToken: String): TokenService? {
        if (serviceToken != this.serviceToken) {
            return null
        }
        return TokenService(
            serviceType = "SERVICE",
            authorities = mutableListOf(SimpleGrantedAuthority(Role.of(Role.SERVICE))),
        )
    }

}
