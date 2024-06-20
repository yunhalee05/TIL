package com.example.springsecurity.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    private val authorizationHeader = "Authorization"
    private val serviceTypeHeader = "Service-Type"

    @Bean
    fun openApi(): OpenAPI {
        return OpenAPI()
            // OpenApi의 기본 설정
            .info(
                Info()
                    // swagger 문서 제목
                    .title("Spring Security")
                    // swagger 문서 설명
                    .description("Spring Security springdoc-openapi swagger-ui")
                    // Swagger 문서 버전 표기
                    .version("v1.0")
            )
            .components(headerComponents())
            .security(listOf(SecurityRequirement().addList(authorizationHeader, serviceTypeHeader)))
    }

    private fun headerComponents(): Components {
        return Components()
            .addSecuritySchemes(authorizationHeader, bearerSecuritySchema())
            .addSecuritySchemes(serviceTypeHeader, serviceTypeSecuritySchema())
    }

    private fun bearerSecuritySchema(): SecurityScheme = SecurityScheme()
        .type(SecurityScheme.Type.HTTP)
        .name(authorizationHeader)
        .scheme("bearer")
        .`in`(SecurityScheme.In.HEADER)
        .description("Bearer Authentication")

    private fun serviceTypeSecuritySchema(): SecurityScheme = SecurityScheme()
        .type(SecurityScheme.Type.APIKEY)
        .name(serviceTypeHeader)
        .`in`(SecurityScheme.In.HEADER)
        .description("Service-Type Authentication")

    // GroupedOpenApi의 select a definition을 통해 API를 그룹화
    // 애플리케이션이 충분히 클때 사용하면 구분하기 좋음
    @Bean
    fun testApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            // swagger 문서에서의 definition 이름
            .group("test")
            // url을 통해 그룹을 나눔
            .pathsToMatch("/api/v1/test/**")
            // 그룹에서 스캔할 패키지
            .packagesToScan("com.example.springsecurity")
            .build()
    }

    @Bean
    fun memberApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            // swagger 문서에서의 definition 이름
            .group("member")
            // url을 통해 그룹을 나눔
            .pathsToMatch("/api/v1/test/**")
            // 그룹에서 스캔할 패키지
            .packagesToScan("com.example.springsecurity")
            .build()
    }
}
