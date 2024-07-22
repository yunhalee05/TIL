package com.example.springsecurity.config.security.authprovider

import com.example.springsecurity.config.security.Role
import com.example.springsecurity.config.security.authentication.token.AuthenticationHeaderToken
import com.example.springsecurity.config.security.authentication.TokenAuthentication
import com.example.springsecurity.config.security.authentication.TokenMember
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component

@Component
class AuthenticationTokenAuthProvider: AuthenticationProvider {

    private val memberToken = "MEMBER"
    override fun authenticate(authentication: Authentication): TokenAuthentication? {
        val authenticationToken = authentication.credentials as String?
        if (authenticationToken.isNullOrBlank()) {
            return null
        }
        return TokenAuthentication(convertMemberDetails(authenticationToken))
    }

    override fun supports(authentication: Class<*>): Boolean {
        return authentication == AuthenticationHeaderToken::class.java
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
}
