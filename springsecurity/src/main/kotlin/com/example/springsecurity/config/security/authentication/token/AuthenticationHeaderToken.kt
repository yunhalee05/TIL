package com.example.springsecurity.config.security.authentication.token

class AuthenticationHeaderToken(
    private val token: String?
) : SimpleAuthenticationToken(token)