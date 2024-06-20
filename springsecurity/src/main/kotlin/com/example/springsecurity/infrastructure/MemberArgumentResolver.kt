package com.example.springsecurity.infrastructure

import com.example.springsecurity.config.security.authentication.TokenAuthentication
import com.example.springsecurity.config.security.authentication.TokenMember
import org.springframework.core.MethodParameter
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class MemberArgumentResolver : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.parameterType == TokenMember::class.java
    }

    override fun resolveArgument(parameter: MethodParameter, mavContainer: ModelAndViewContainer?, webRequest: NativeWebRequest, binderFactory: WebDataBinderFactory?): Any? {
        val principals = SecurityContextHolder.getContext().authentication.principal as MutableList<TokenAuthentication>
        principals.forEach { principal ->
            if (principal.tokenUser is TokenMember) {
                return principal.tokenUser
            }
        }
        return null
    }
}
