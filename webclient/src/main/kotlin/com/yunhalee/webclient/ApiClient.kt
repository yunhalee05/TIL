package com.yunhalee.webclient

import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriBuilder
import org.springframework.web.util.UriComponentsBuilder
import org.springframework.web.util.UriUtils
import reactor.util.retry.Retry
import java.net.SocketException
import java.net.URI
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
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

    fun <T, E : RuntimeException> getForObject(path: String, queryParams: MultiValueMap<String, String>, returnType: Class<T>, exception: Class<E>): T? {
        return client.get()
            .uri { builder: UriBuilder ->
                builder
                    .path(path)
                    // queryParams 로 바로 지정하려면, multivalueMap을 사용해야 한다. (기본 설정)
                    .queryParams(queryParams)
                    .build()
            }
            .retrieve()
            .bodyToMono(returnType)
            .retryWhen(Retry.backoff(3, Duration.ofMillis(1000)).filter { it is SocketException })
            .timeout(Duration.ofMillis(3000L))
            .onErrorMap { throwable -> exception.getDeclaredConstructor(String::class.java).newInstance(throwable.message) }
            .block()
    }

    fun <T, E : RuntimeException> getForObjectWithUrlEncoder(path: String, queryParams: MultiValueMap<String, String>, returnType: Class<T>, exception: Class<E>): T? {
        // encoding 된 걸 사용하려면 각각 문자열을 모두 encoding 해야 함
        val encodedParams = queryParams.entries.joinToString("&") { entry ->
            entry.value.joinToString("&") { value ->
                "${URLEncoder.encode(entry.key, StandardCharsets.UTF_8.toString())}=${URLEncoder.encode(value, StandardCharsets.UTF_8.toString())}"
            }
        }
        val uri = URI.create("http://localhost:8080$path?$encodedParams")
        return client.get()
            .uri(uri)
            .retrieve()
            .bodyToMono(returnType)
            .retryWhen(Retry.backoff(3, Duration.ofMillis(1000)).filter { it is SocketException })
            .timeout(Duration.ofMillis(3000L))
            .onErrorMap { throwable -> exception.getDeclaredConstructor(String::class.java).newInstance(throwable.message) }
            .block()
    }




}