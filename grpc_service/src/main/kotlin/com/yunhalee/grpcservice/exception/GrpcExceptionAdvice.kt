package com.yunhalee.grpcservice.exception

import io.grpc.StatusException
import io.grpc.StatusRuntimeException
import net.devh.boot.grpc.server.advice.GrpcAdvice
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler
import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment

@GrpcAdvice
class GrpcExceptionAdvice(
    private val env: Environment,
) {
    val log = LoggerFactory.getLogger(GrpcExceptionAdvice::class.java)

    @GrpcExceptionHandler(RuntimeException::class)
    fun handleException(exception: RuntimeException): StatusRuntimeException {
        val standardErrorType = GrpcStandardErrorType.of(exception)
        if (standardErrorType.isErrorLogRequired()) {
            log.error(exception.message, exception)
        } else {
            log.error(exception.message)
        }
        return standardErrorType.status.withDescription(exception.message)
            .withCause(exception)
            .asRuntimeException(GrpcErrorResponse.of(exception, standardErrorType, env))
    }

    @GrpcExceptionHandler(Exception::class)
    fun handleException(exception: Exception): StatusException {
        val standardErrorType = GrpcStandardErrorType.of(exception)
        if (standardErrorType.isErrorLogRequired()) {
            log.error(exception.message, exception)
        } else {
            log.error(exception.message)
        }
        return standardErrorType.status.withDescription(exception.message)
            .withCause(exception)
            .asException(GrpcErrorResponse.of(exception, standardErrorType, env))
    }
}
