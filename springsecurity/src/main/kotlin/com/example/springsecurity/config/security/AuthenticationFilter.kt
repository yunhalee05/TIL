package com.example.springsecurity.config.security

import com.example.springsecurity.config.security.authentication.MultiAuthentications
import com.example.springsecurity.config.security.authprovider.AuthenticationTokenAuthProvider
import com.example.springsecurity.config.security.authprovider.ServiceTokenAuthProvider
import com.example.springsecurity.exception.UnAuthenticatedException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class AuthenticationFilter(
    private val authenticationTokenAuthProvider: AuthenticationTokenAuthProvider,
    private val serviceTokenAuthProvider: ServiceTokenAuthProvider,
) : OncePerRequestFilter() {
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
        val accessToken = resolveAuthorizationToken(request)
        val serviceTypeToken = resolveServiceTypeToken(request)
        val authentication =
            MultiAuthentications(
                listOfNotNull(
                    serviceTokenAuthProvider.authentication(serviceTypeToken),
                    authenticationTokenAuthProvider.authentication(accessToken),
                ).toMutableList(),
            )
        if (!authentication.isAuthenticated()) {
            throw UnAuthenticatedException("인증되지 않은 사용자입니다.")
        }
        SecurityContextHolder.getContext().authentication = authentication
    }

    private fun resolveAuthorizationToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        if ((!bearerToken.isNullOrBlank()) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7)
        }
        return null
    }

    private fun resolveServiceTypeToken(request: HttpServletRequest): String? {
        val serviceType = request.getHeader("Service-Type")
        if (request.getHeader("Service-Type").isNullOrBlank()) {
            return null
        }
        return serviceType
    }
}
