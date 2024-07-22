package com.example.springsecurity.config.security.authentication.token

class ServiceHeaderToken(
    private val token: String?
) : SimpleAuthenticationToken(token)