package com.yunhalee.webclient

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.codec.ClientCodecConfigurer
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.util.DefaultUriBuilderFactory

@Configuration
class WebclientConfig {

    private val objectMapper = ObjectMapper().registerModule(KotlinModule()).registerModule(JavaTimeModule())

    @Bean
    fun webclientWithUriFactory(objectMapper: ObjectMapper): ApiClient {
        return ApiClient(
            WebclientBuilder()
                .exchangeStrategies(
                    ExchangeStrategies.builder()
                        .codecs { configurer: ClientCodecConfigurer -> configurer.defaultCodecs().jackson2JsonDecoder(Jackson2JsonDecoder(objectMapper)) }
                        .build()
                )
                .uriFactory(DefaultUriBuilderFactory().apply { encodingMode = DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY })
                .build()
        )
    }

    @Bean
    fun webclientWithoutUriFactory(objectMapper: ObjectMapper): ApiClient {
        return ApiClient(
            WebclientBuilder()
                .exchangeStrategies(
                    ExchangeStrategies.builder()
                        .codecs { configurer: ClientCodecConfigurer -> configurer.defaultCodecs().jackson2JsonDecoder(Jackson2JsonDecoder(objectMapper)) }
                        .build()
                )
                .build()
        )
    }
}
