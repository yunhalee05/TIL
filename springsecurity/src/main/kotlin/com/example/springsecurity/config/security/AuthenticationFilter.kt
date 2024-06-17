package com.example.springsecurity.config.security

import com.example.springsecurity.exception.UnAuthenticatedException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.filter.OncePerRequestFilter

class AuthenticationFilter : OncePerRequestFilter() {
    companion object {
        const val EXCEPTION_ATTRIBUTE = "exception"
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        try {
            authenticate(request)
        } catch (e: UnAuthenticatedException) {
            request.setAttribute(EXCEPTION_ATTRIBUTE, e)
        }
        filterChain.doFilter(request, response)
    }

    private fun authenticate(request: HttpServletRequest) {
        TODO()
    }
}
