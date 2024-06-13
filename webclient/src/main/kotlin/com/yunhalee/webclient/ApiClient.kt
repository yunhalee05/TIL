package com.yunhalee.webclient

import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriBuilder
import reactor.util.retry.Retry
import java.net.SocketException
import java.time.Duration

class ApiClient(
    val client: WebClient
) {

    // + 가 문자열에 들어가게 되면 UriComponentBuilder가 올바르게 동작하지 않는다.
    // +, " ", "&", "="
    // +는 decode 될 때 빈 문자열로 디코드 된다.
    // DefaultUriBuilderFactory internally uses UriComponentsBuilder and essentially does this:


    fun <T, E : RuntimeException> getForObjectWithEncodedQueryParamsWithNumber(path: String, queryParams: Map<String, String>, returnType: Class<T>, exception: Class<E>): T? {
        return client.get()
            .uri { builder: UriBuilder ->
                builder.path(path)
                    .apply {
                        queryParams.keys.forEach { key ->
                            if (key.toIntOrNull() == null) {
                                queryParam(key, "{$key}")
                            }
                        }
                    }
                    .build(queryParams)
            }
            .retrieve()
            .bodyToMono(returnType)
            .retryWhen(Retry.backoff(3, Duration.ofMillis(1000)).filter { it is SocketException })
            .timeout(Duration.ofMillis(3000L))
            .onErrorMap { throwable -> exception.getDeclaredConstructor(String::class.java).newInstance(throwable.message) }
            .block()
    }

    fun <T, E : RuntimeException> getForObjectWithEncodedQueryParamsWithoutNumber(path: String, queryParams: Map<String, String>, returnType: Class<T>, exception: Class<E>): T? {
        return client.get()
            .uri { builder: UriBuilder ->
                builder.path(path)
                    .apply {
                        queryParams.keys.forEach { key ->
                            queryParam(key, "{$key}")

                        }
                    }
                    .build(queryParams)
            }
            .retrieve()
            .bodyToMono(returnType)
            .retryWhen(Retry.backoff(3, Duration.ofMillis(1000)).filter { it is SocketException })
            .timeout(Duration.ofMillis(3000L))
            .onErrorMap { throwable -> exception.getDeclaredConstructor(String::class.java).newInstance(throwable.message) }
            .block()
    }

    fun <T, E : RuntimeException> getForObject(path: String, queryParams: Map<String, String>, returnType: Class<T>, exception: Class<E>): T? {
        println(queryParams)
        return client.get()
            .uri { builder: UriBuilder ->
                builder
                    .path(path)
                    .build(queryParams)
            }
            .retrieve()
            .bodyToMono(returnType)
            .retryWhen(Retry.backoff(3, Duration.ofMillis(1000)).filter { it is SocketException })
            .timeout(Duration.ofMillis(3000L))
            .onErrorMap { throwable -> exception.getDeclaredConstructor(String::class.java).newInstance(throwable.message) }
            .block()
    }

}