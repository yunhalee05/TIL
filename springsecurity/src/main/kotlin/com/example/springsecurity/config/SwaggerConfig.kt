package com.example.springsecurity.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun openApi(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Spring Security")
                    .description("Spring Security springdoc-openapi swagger-ui")
                    .version("v1.0")
            )
    }

    @Bean
    fun api(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("springdoc-openapi")
            .pathsToMatch("/api/v1/**")
            .packagesToScan("com.example.springsecurity")
            .build()
    }
}