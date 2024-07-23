package com.example.springsecurity.config.security.authprovider

import com.example.springsecurity.config.security.Role
import com.example.springsecurity.config.security.authentication.MultiAuthentications
import com.example.springsecurity.config.security.authentication.token.ServiceHeaderToken
import com.example.springsecurity.config.security.authentication.TokenAuthentication
import com.example.springsecurity.config.security.authentication.TokenService
import com.example.springsecurity.config.security.authentication.token.HeaderToken
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class ServiceTokenAuthProvider: AuthenticationProvider {

    private val serviceToken = "SERVICE"
    override fun authenticate(authentication: Authentication?): TokenAuthentication? {
        val serviceToken = (authentication as? HeaderToken)?.getServiceTypeToken()
        if (serviceToken.isNullOrEmpty()) {
            return null
        }
        val authentication = TokenAuthentication(convertMemberDetails(serviceToken))
//        return provide(authentication)
        return authentication
    }

    override fun supports(authentication: Class<*>): Boolean {
        return authentication == HeaderToken::class.java
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

    private fun provide(authentication: TokenAuthentication): MultiAuthentications {
        val currentAuthentication = SecurityContextHolder.getContext().authentication
        val authentications =
            (currentAuthentication as? MultiAuthentications)?.authentications?.apply {
                add(authentication)
            } ?: mutableListOf(authentication)
        val multiUserAuthentication = MultiAuthentications(authentications)
        SecurityContextHolder.getContext().authentication = multiUserAuthentication
        return multiUserAuthentication
    }

}
