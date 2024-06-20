package com.example.springsecurity.config.security.authentication

import org.springframework.security.core.GrantedAuthority

data class TokenMember(
    val age: Int,
    override val authorities: MutableList<GrantedAuthority>,
) : TokenUser() {
    override fun getPrincipal() = this
    override fun getName(): String = "MEMBER"
}
