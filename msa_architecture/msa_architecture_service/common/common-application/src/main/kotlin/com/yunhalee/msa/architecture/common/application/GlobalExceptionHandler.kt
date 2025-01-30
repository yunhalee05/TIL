package com.yunhalee.msa.architecture.common.application

import jakarta.validation.ConstraintViolationException
import jakarta.validation.ValidationException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.logging.Logger
import java.util.stream.Collectors
import kotlin.Exception

@ControllerAdvice
class GlobalExceptionHandler {


    private val logger = Logger.getLogger(GlobalExceptionHandler::class.java.name)


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException::class)
    @ResponseBody
    fun handleException(e: ValidationException): ErrorDto {
        logger.warning("Exception occurred : ${e.message}")
        if (e is ConstraintViolationException) {
            val violations = getConstraintViolationMessage(e)
            logger.warning("Constraint violation occurred : $violations")
            return ErrorDto.builder()
                .code(HttpStatus.BAD_REQUEST.reasonPhrase)
                .message(violations)
                .build()
        }
        logger.warning("ValidationException occurred : ${e.message}")
        return ErrorDto.builder()
            .code(HttpStatus.BAD_REQUEST.reasonPhrase)
            .message(e.message!!)
            .build()
    }

    private fun getConstraintViolationMessage(e: ConstraintViolationException): String {
        return e.constraintViolations.stream().map { it.message }.collect(Collectors.joining("--"))
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception::class)
    @ResponseBody
    fun handleException(e: Exception): ErrorDto {
        logger.warning("Exception occurred : ${e.message}")
        return ErrorDto.builder()
            .code(HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase)
            .message("Unexpected error occurred : ${e.message!!}")
            .build()
    }

}