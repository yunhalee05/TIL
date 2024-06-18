package com.yunhalee.study.config

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.FilterConfig
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class LoggingFilter : Filter {

    private val logger = LoggerFactory.getLogger(LoggingFilter::class.java)

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpServletRequest = request as HttpServletRequest
        val httpServletResponse = response as HttpServletResponse

        logger.info(httpServletRequest.requestURI + httpServletRequest.queryString)
        logger.info("Incoming request: ${httpServletRequest.method} ${httpServletRequest.requestURI}")
        chain.doFilter(request, response)
        logger.info("Outgoing response: ${httpServletResponse.status}")
    }

    override fun init(filterConfig: FilterConfig?) {
    }

    override fun destroy() {
    }
}
