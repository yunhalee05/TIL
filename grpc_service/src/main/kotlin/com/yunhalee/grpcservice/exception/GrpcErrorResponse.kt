package com.yunhalee.grpcservice.exception


import com.yunhalee.ErrorResponse
import io.grpc.Metadata
import io.grpc.protobuf.ProtoUtils
import org.springframework.core.env.Environment
import org.springframework.core.env.Profiles

class GrpcErrorResponse {
    companion object {
        fun of(
            exception: Exception,
            standardErrorType: GrpcStandardErrorType,
            env: Environment,
        ): Metadata {
            val errorResponseKey: Metadata.Key<ErrorResponse> = ProtoUtils.keyForProto(ErrorResponse.getDefaultInstance())
            val errorResponse: ErrorResponse = errorResponseOf(exception, env, standardErrorType)
            val metadata = Metadata()
            metadata.put(errorResponseKey, errorResponse)
            return metadata
        }

        private fun errorResponseOf(
            exception: Exception,
            env: Environment,
            standardErrorType: GrpcStandardErrorType,
        ): ErrorResponse {
            val message = exception.message ?: standardErrorType.getMessage()
            return ErrorResponse.newBuilder()
                .setMessage(message)
                .setCode(standardErrorType.getCode().toString())
//                .setStatus(standardErrorType.getStatus())
//                .setDebugMessage(getDebugMessage(env, exception))
                .build()
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
    }
}
