package com.yunhalee.grpcservice.config

import com.yunhalee.ErrorResponse
import io.grpc.Metadata
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.grpc.protobuf.ProtoUtils
import net.devh.boot.grpc.server.advice.GrpcAdvice
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler

@GrpcAdvice
class ControllerAdvice {

    @GrpcExceptionHandler(Exception::class)
    fun handleException(e: Exception): StatusRuntimeException {
        val metadataKey: Metadata.Key<ErrorResponse> = ProtoUtils.keyForProto(ErrorResponse.getDefaultInstance())
        val metadata = Metadata()
        metadata.put(metadataKey, ErrorResponse.newBuilder().setMessage("예시 테스트 예외가 발생했습니다").setCode("50001").build())
        return Status.UNKNOWN
            .withDescription("Internal server error")
            .asRuntimeException(metadata)
    }
}
