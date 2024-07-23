package com.example.springsecurity.config

import com.example.springsecurity.config.security.RestAccessDeniedHandler
import com.example.springsecurity.config.security.RestAuthenticationEntryPoint
import com.example.springsecurity.config.security.Role
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig(
    val restAuthenticationEntryPoint: RestAuthenticationEntryPoint,
    val restAccessDeniedHandler: RestAccessDeniedHandler,
) {
    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { obj: CsrfConfigurer<HttpSecurity> -> obj.disable() }
            .cors { obj: CorsConfigurer<HttpSecurity> -> obj.disable() }
            .httpBasic { obj: HttpBasicConfigurer<HttpSecurity> -> obj.disable() }
            .formLogin { obj: FormLoginConfigurer<HttpSecurity> -> obj.disable() }
            .authorizeHttpRequests { authorizeRequests ->
                authorizeRequests
                    .requestMatchers(HttpMethod.GET, "/admin/**").hasAnyRole("ADMIN")
                    .requestMatchers(HttpMethod.POST, "/api/v1/test").hasAnyRole(Role.MEMBER, Role.SERVICE)
                    .anyRequest().authenticated()
            }
//            .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling { exceptionHandling ->
                exceptionHandling
                    .authenticationEntryPoint(restAuthenticationEntryPoint)
                    .accessDeniedHandler(restAccessDeniedHandler)
            }
            .sessionManagement { sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .headers { headers -> headers.frameOptions { frameOptions -> frameOptions.disable() } }
        return http.build()
    }

    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer {
        return WebSecurityCustomizer {
            web: WebSecurity ->
            web.ignoring().requestMatchers(
                "/swagger-ui/**",
                "/swagger-resources/**",
                "/v3/api-docs/**",
                "/v3/api-docs.yaml",
                "/api-docs/**",
                "/actuator/**",
                "/swagger-ui.html"
            )
        }
    }
}
