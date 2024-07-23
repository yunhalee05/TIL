package com.example.springsecurity.config.security.authprovider

import com.example.springsecurity.config.security.Role
import com.example.springsecurity.config.security.authentication.MultiAuthentications
import com.example.springsecurity.config.security.authentication.token.AuthenticationHeaderToken
import com.example.springsecurity.config.security.authentication.TokenAuthentication
import com.example.springsecurity.config.security.authentication.TokenMember
import com.example.springsecurity.config.security.authentication.token.HeaderToken
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class AuthenticationTokenAuthProvider: AuthenticationProvider {

    private val memberToken = "MEMBER"
    override fun authenticate(authentication: Authentication?): TokenAuthentication? {
        val authenticationToken = (authentication as? HeaderToken)?.getAuthenticationToken()
        if (authenticationToken.isNullOrBlank()) {
            return null
        }
        val authentication = TokenAuthentication(convertMemberDetails(authenticationToken))
//        return provide(authentication)
        return authentication
    }

    override fun supports(authentication: Class<*>): Boolean {
        return authentication == HeaderToken::class.java
    }

    private fun convertMemberDetails(authorizationToken: String): TokenMember? {
        if (authorizationToken != memberToken) {
            return null
        }
        return TokenMember(
            age = 20,
            authorities = mutableListOf(SimpleGrantedAuthority(Role.of(Role.MEMBER))),
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
