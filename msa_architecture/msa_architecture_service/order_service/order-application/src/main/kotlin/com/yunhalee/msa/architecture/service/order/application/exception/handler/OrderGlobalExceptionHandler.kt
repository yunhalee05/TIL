package com.yunhalee.msa.architecture.service.order.application.exception.handler

import com.yunhalee.msa.architecture.common.application.ErrorDto
import com.yunhalee.msa.architecture.service.order.domain.core.exception.OrderDomainException
import com.yunhalee.msa.architecture.service.order.domain.core.exception.OrderNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.logging.Logger

@ControllerAdvice
class OrderGlobalExceptionHandler {


    private val logger = Logger.getLogger(OrderGlobalExceptionHandler::class.java.name)

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(OrderDomainException::class)
    fun handleException(e: OrderDomainException): ErrorDto {
        logger.warning("OrderDomainException occurred : ${e.message}")
        return ErrorDto.builder()
            .code(HttpStatus.BAD_REQUEST.reasonPhrase)
            .message(e.message!!)
            .build()
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(OrderNotFoundException::class)
    fun handleException(e: OrderNotFoundException): ErrorDto {
        logger.warning("OrderNotFoundException occurred : ${e.message}")
        return ErrorDto.builder()
            .code(HttpStatus.NOT_FOUND.reasonPhrase)
            .message(e.message!!)
            .build()
    }
}