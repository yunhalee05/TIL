package com.example.springsecurity.config

import com.example.springsecurity.infrastructure.resolver.MemberArgumentResolver
import com.example.springsecurity.infrastructure.resolver.ServiceArgumentResolver
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    private val memberArgumentResolver: MemberArgumentResolver,
    private val serviceArgumentResolver: ServiceArgumentResolver
) : WebMvcConfigurer {

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.addAll(
            listOf(
                memberArgumentResolver,
                serviceArgumentResolver
            )
        )
    }
}
