package com.example.springsecurity.config.security.authprovider

import com.example.springsecurity.config.security.Role
import com.example.springsecurity.config.security.authentication.TokenAuthentication
import com.example.springsecurity.config.security.authentication.TokenService
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component

@Component
class ServiceTokenAuthProvider {

    private val serviceToken = "SERVICE"
    fun authentication(serviceToken: String?): TokenAuthentication? {
        if (serviceToken == null) {
            return null
        }
        return TokenAuthentication(convertMemberDetails(serviceToken))
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
