package com.example.springsecurity.config.security.authentication.token

import org.springframework.security.authentication.AbstractAuthenticationToken

open class SimpleAuthenticationToken(
    private val token: String?,
    private val principal: Any? = null,
) : AbstractAuthenticationToken(listOf()) {

    init {
        isAuthenticated = false
    }

    override fun getCredentials(): Any = token ?: ""

    override fun getPrincipal(): Any? = principal
}