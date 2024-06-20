package com.example.springsecurity.config.security

class Role {
    companion object {
        const val MEMBER = "MEMBER"
        const val SERVICE = "SERVICE"

        fun of(role: String) = "ROLE_$role"
    }
}
