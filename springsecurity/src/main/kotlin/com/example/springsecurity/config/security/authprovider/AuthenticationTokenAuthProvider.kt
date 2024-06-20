package com.example.springsecurity.config.security.authprovider

import com.example.springsecurity.config.security.Role
import com.example.springsecurity.config.security.authentication.TokenAuthentication
import com.example.springsecurity.config.security.authentication.TokenMember
import org.springframework.security.core.authority.SimpleGrantedAuthority

class AuthenticationTokenAuthProvider {

    private val memberToken = "MEMBER"
    fun authentication(authenticationToken: String?): TokenAuthentication? {
        if (authenticationToken == null) {
            return null
        }
        return TokenAuthentication(convertMemberDetails(authenticationToken))
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
