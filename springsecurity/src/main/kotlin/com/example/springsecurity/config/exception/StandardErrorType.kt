package com.example.springsecurity.config.exception

import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageConversionException
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.InsufficientAuthenticationException
import org.springframework.validation.BindException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import java.net.http.HttpTimeoutException

enum class StandardErrorType(
    val status: HttpStatus,
    val code: Int,
    val message: String,
    val exceptions: Set<Any>,
) {
    APPLICATION_TIMEOUT(HttpStatus.INTERNAL_SERVER_ERROR, 50001, "gateway time out", setOf(HttpTimeoutException::class)),

    // default
    BAD_REQUEST(
        HttpStatus.BAD_REQUEST,
        40000,
        "bad request",
        setOf(
            IllegalArgumentException::class,
            IllegalStateException::class,
            MethodArgumentNotValidException::class,
            MethodArgumentTypeMismatchException::class,
            HttpMessageConversionException::class,
            HttpMessageNotReadableException::class,
            BindException::class,
        ),
    ),
    INSUFFICIENT_AUTHENTICATION(HttpStatus.UNAUTHORIZED, 40100, "unauthorized service", setOf(InsufficientAuthenticationException::class)),
    INVALID_API_REQUEST(HttpStatus.FORBIDDEN, 40300, "access denied service", setOf(AccessDeniedException::class)),
    NOT_FOUND(HttpStatus.NOT_FOUND, 40400, "not found", setOf()),
    METHOD_NOT_ALLOWED(
        HttpStatus.METHOD_NOT_ALLOWED,
        40500,
        "not supported api method",
        setOf(HttpRequestMethodNotSupportedException::class),
    ),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 50001, "internal server error", setOf(Exception::class)),
    ;

    companion object {
        fun of(throwable: Throwable): StandardErrorType {
            return values().find { it.exceptions.contains(throwable::class) } ?: INTERNAL_SERVER_ERROR
        }
    }
}
