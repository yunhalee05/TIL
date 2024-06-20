package com.example.springsecurity.config.security.authentication

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

class TokenAuthentication(
    val tokenUser: TokenUser?,
) : Authentication {

    private var authenticated: Boolean = tokenUser != null
    override fun getName(): String? = tokenUser?.name

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = tokenUser?.authorities ?: mutableListOf()

    override fun getCredentials(): Any? = null

    override fun getDetails(): Any? = tokenUser?.principal

    override fun getPrincipal(): Any? = tokenUser?.principal

    override fun isAuthenticated(): Boolean = authenticated

    override fun setAuthenticated(isAuthenticated: Boolean) {
        authenticated == isAuthenticated
    }
}
