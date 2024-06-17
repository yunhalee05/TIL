package com.example.springsecurity.config.exception

import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.core.env.Environment
import org.springframework.core.env.Profiles
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException

@Schema(description = "에러 응답")
data class ErrorResponse(
    @get:Schema(description = "에러 메시지", required = true, name = "message", example = "에러 메시지")
    val message: String,
    @get:Schema(description = "에러 상세 코드", required = true, name = "code", example = "50101")
    val code: String,
    @get:Schema(description = "HTTP 상태 코드", required = true, name = "status", example = "500")
    val httpStatus: HttpStatus,
    @get:Schema(description = "디버그 메시지. 개발 환경에서만 제공", required = false, name = "debugMessage", example = "에러 메시지 ( 개발환경만 제공 )")
    val debugMessage: String? = null,
) {
    companion object {
        fun of(
            exception: Exception,
            env: Environment,
        ): ErrorResponse {
            val dataErrorType = StandardErrorType.of(exception)
            val message = getMessage(exception, dataErrorType)
            return ErrorResponse(
                message = message,
                code = dataErrorType.code.toString(),
                httpStatus = dataErrorType.status,
                debugMessage = getDebugMessage(env, exception),
            )
        }

        private fun getMessage(
            exception: Exception,
            dataErrorType: StandardErrorType,
        ): String {
            return when (exception) {
                is MethodArgumentNotValidException -> getMethodArgumentNotValidExceptionMessage(exception)
                is HttpMessageNotReadableException -> geHttpMessageNotReadableExceptionMessage(exception) ?: dataErrorType.message
                else -> exception.message ?: dataErrorType.message
            }
        }

        private fun getDebugMessage(
            env: Environment,
            exception: Exception,
        ): String? {
            if (env.acceptsProfiles(Profiles.of("prod"))) {
                return null
            }
            return exception.stackTraceToString()
        }

        private fun getMethodArgumentNotValidExceptionMessage(exception: MethodArgumentNotValidException): String {
            val error = exception.bindingResult.fieldErrors.first()
            return "field : ${error.field}, message :  ${error.defaultMessage}"
        }

        private fun geHttpMessageNotReadableExceptionMessage(exception: HttpMessageNotReadableException): String? {
            return when (val causeException = exception.cause) {
                is MissingKotlinParameterException -> getMissingKotlinParameterException(causeException)
                else -> exception.message
            }
        }

        private fun getMissingKotlinParameterException(exception: MissingKotlinParameterException): String {
            return "파라미터가 누락되었습니다. 필드명: ${exception.parameter.name}"
        }
    }
}
