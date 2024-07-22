package com.example.springsecurity.config.security.authentication

import org.springframework.security.core.GrantedAuthority

open class TokenUser {
    open val authorities: MutableList<GrantedAuthority>
        get() {
            return this.authorities
        }

    open fun getPrincipal(): Any? {
        return this.getPrincipal()
    }

    open fun getName(): String? {
        return this.getName()
    }
}
