package com.example.springsecurity.config.security.authentication

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

class MultiAuthentications(
    val authentications: MutableList<TokenAuthentication>,
) : Authentication {

    private var authenticated: Boolean = authentications.isNotEmpty()
    override fun getName(): String = authentications.map { it.name }.joinToString { "_" }

    override fun getAuthorities(): Collection<GrantedAuthority> = authentications.flatMap { it.authorities }

    override fun getCredentials(): Any? = null

    override fun getDetails(): Any = authentications

    override fun getPrincipal(): Any = authentications

    override fun isAuthenticated(): Boolean = authenticated

    override fun setAuthenticated(isAuthenticated: Boolean) {
        authenticated == isAuthenticated
    }
}
