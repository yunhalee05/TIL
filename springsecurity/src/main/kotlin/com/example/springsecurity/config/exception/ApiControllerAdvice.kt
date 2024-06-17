package com.example.springsecurity.config.exception

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageConversionException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.client.HttpClientErrorException

@ControllerAdvice
class ApiControllerAdvice(
    private val environment: Environment,
) {

    private val logger: Logger = LoggerFactory.getLogger(ApiControllerAdvice::class.java.name)

    @ExceptionHandler(HttpClientErrorException::class)
    fun exceptionHandle(exception: HttpClientErrorException): ResponseEntity<ErrorResponse> {
        logger.error(exception.message, exception)
        val standardError = ErrorResponse.of(exception, environment)
        return ResponseEntity(standardError, exception.statusCode)
    }

    @ExceptionHandler(HttpMessageConversionException::class)
    fun exceptionHandle(exception: HttpMessageConversionException): ResponseEntity<ErrorResponse> {
        logger.error(exception.message, exception)
        val standardError = ErrorResponse.of(exception, environment)
        return ResponseEntity(standardError, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class, RuntimeException::class)
    fun exceptionHandle(exception: Exception): ResponseEntity<ErrorResponse> {
        val standardError = ErrorResponse.of(exception, environment)
        if (standardError.httpStatus.is5xxServerError) {
            logger.error(exception.message, exception)
        } else {
            logger.warn(exception.message, exception)
        }
        return ResponseEntity(standardError, standardError.httpStatus)
    }
}
