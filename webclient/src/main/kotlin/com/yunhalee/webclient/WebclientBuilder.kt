package com.yunhalee.webclient

import io.netty.handler.logging.LogLevel
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriBuilderFactory
import reactor.core.publisher.Mono
import reactor.netty.http.client.HttpClient
import reactor.netty.resources.ConnectionProvider
import reactor.netty.transport.logging.AdvancedByteBufFormat
import java.time.Duration
import java.util.function.Consumer


class WebClientBuilder {
    private val webClient = WebClient.builder()
    private val log = LoggerFactory.getLogger(this::class.java)

    private val headers = mutableMapOf(HttpHeaders.CONTENT_TYPE to MediaType.APPLICATION_JSON_VALUE)
    fun addHeader(header: Pair<String, String>): WebClientBuilder = headers.put(header.first, header.second).let { this }

    fun build(): WebClient {
        val provider = ConnectionProvider.builder("accounts")
            .maxConnections(100)
            .maxIdleTime(Duration.ofSeconds(48))
            .maxLifeTime(Duration.ofSeconds(48))
            .pendingAcquireTimeout(Duration.ofMillis(5000))
            .pendingAcquireMaxCount(-1)
            .evictInBackground(Duration.ofSeconds(30))
            .build()
        return webClient.baseUrl("http://localhost:8080/test")
            .clientConnector(ReactorClientHttpConnector(
                HttpClient
                    .create(provider)
                    // logging 옵션 추가
//                    .wiretap(true)
                    .wiretap("com.yunhalee.webclient", LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL)
                    .wiretap("reactor.netty.http.client.HttpClient", LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL)
        )
            )
            .exchangeStrategies(ExchangeStrategies.builder().codecs { it.defaultCodecs().enableLoggingRequestDetails(true) }.build())
//            .codecs { it.defaultCodecs().enableLoggingRequestDetails(true) }
            .defaultHeaders { headers.forEach { header -> it.add(header.key, header.value) } }

            .build()
    }

    private fun logRequest(): ExchangeFilterFunction {
        return ExchangeFilterFunction.ofRequestProcessor { clientRequest: ClientRequest ->
            log.info("Request: {} {}", clientRequest.method(), clientRequest.url())
            clientRequest.headers().forEach { name: String, values: List<String> -> values.forEach(Consumer { value: String? -> log.info("{}={}", name, value) }) }
            Mono.just(clientRequest)
        }
    }

    fun exchangeStrategies(exchangeStrategies: ExchangeStrategies): WebClientBuilder {
        webClient.exchangeStrategies(exchangeStrategies)
        return this
    }

    fun uriFactory(factory: UriBuilderFactory): WebClientBuilder {
        webClient.uriBuilderFactory(factory)
        return this
    }
}