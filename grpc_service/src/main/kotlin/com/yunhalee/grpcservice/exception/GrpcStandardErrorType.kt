package com.yunhalee.grpcservice.exception


import io.grpc.Status
import org.springframework.validation.BindException

enum class GrpcStandardErrorType(
    val status: Status,
    private val code: Int,
    private val message: String,
    val exceptions: Set<Any>,
) {
    // 1
    CANCELLED(Status.CANCELLED, 1, "canceled", setOf()),

    // 3
    INVALID_ARGUMENT(
        Status.INVALID_ARGUMENT,
        3,
        "invalid argument",
        setOf(IllegalArgumentException::class),
    ),

    // 4
    DEADLINE_EXCEEDED(Status.DEADLINE_EXCEEDED, 4, "deadline exceeded", setOf()),

    // 5
    NOT_FOUND(Status.NOT_FOUND, 5, "not found", setOf()),

    // 6
    ALREADY_EXISTS(Status.ALREADY_EXISTS, 6, "already exists", setOf()),

    // 7
    PERMISSION_DENIED(Status.PERMISSION_DENIED, 7, "permission denied", setOf(AccessDeniedException::class)),

    // 8
    RESOURCE_EXHAUSTED(Status.RESOURCE_EXHAUSTED, 8, "resource exhausted", setOf()),

    // 9
    FAILED_PRECONDITION(Status.FAILED_PRECONDITION, 9, "failed precondition", setOf()),

    // 10
    ABORTED(Status.ABORTED, 10, "aborted", setOf()),

    // 11
    OUT_OF_RANGE(Status.OUT_OF_RANGE, 11, "out of range", setOf()),

    // 12
    UNIMPLEMENTED(Status.UNIMPLEMENTED, 12, "unimplemented", setOf()),

    // 13
    INTERNAL(Status.INTERNAL, 13, "internal", setOf()),

    // 14
    UNAVAILABLE(Status.UNAVAILABLE, 14, "unavailable", setOf()),

    // 15
    DATA_LOSS(Status.DATA_LOSS, 15, "data loss", setOf()),

    // 16
    UNAUTHENTICATED(Status.UNAUTHENTICATED, 16, "unauthenticated", setOf()),

    // 2
    UNKNOWN(Status.UNKNOWN, 2, "unknown", setOf(RuntimeException::class, Exception::class)),
    ;

    companion object {
        fun of(throwable: Throwable): GrpcStandardErrorType = values().find { it.exceptions.contains(throwable::class) } ?: UNKNOWN
    }

    fun isErrorLogRequired(): Boolean =
        this.status !in
            listOf(
                Status.INVALID_ARGUMENT,
                Status.FAILED_PRECONDITION,
                Status.OUT_OF_RANGE,
                Status.UNIMPLEMENTED,
                Status.PERMISSION_DENIED,
                Status.UNAUTHENTICATED,
                Status.NOT_FOUND,
            )

    fun getMessage() = message

    fun getCode() = code

    fun getStatus() = status.code.name
}
