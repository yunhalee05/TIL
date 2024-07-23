package com.example.springsecurity.config.security.authentication.token

import org.springframework.security.authentication.AbstractAuthenticationToken

class HeaderToken(
    private val serviceTypeToken: String?,
    private val authenticationToken: String?,
    private val principal: Any? = null,
) : AbstractAuthenticationToken(listOf()) {

    init {
        isAuthenticated = false
    }

    override fun getCredentials(): Any = authenticationToken ?: serviceTypeToken ?: ""

    fun getServiceTypeToken() =  serviceTypeToken

    fun getAuthenticationToken() = authenticationToken

    override fun getPrincipal(): Any? = principal
}