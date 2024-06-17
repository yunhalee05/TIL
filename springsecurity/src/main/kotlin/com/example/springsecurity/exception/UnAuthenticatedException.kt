package com.example.springsecurity.exception

import org.springframework.security.core.AuthenticationException

open class UnAuthenticatedException(message: String) : AuthenticationException(message)
