package com.example.springsecurity.config.security.authentication

import org.springframework.security.core.GrantedAuthority

data class TokenService(
    val serviceType: String,
    override val authorities: MutableList<GrantedAuthority>,
) : TokenUser() {
    override fun getPrincipal() = this
    override fun getName(): String = "SERVICE"
}
