package com.yunhalee.grpcclient.component

import com.yunhalee.ErrorResponse
import com.yunhalee.grpcclient.exception.GrpcServerException
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.grpc.protobuf.ProtoUtils
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterThrowing
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component

@Aspect
@Component
class GrpcExceptionAop {

    @Pointcut("@within(com.yunhalee.grpcclient.support.annotation.GrpcComponent)")
    fun grpcClientService() {}

    @AfterThrowing(pointcut = "grpcClientService()", throwing = "ex")
    fun handleGrpcClientServiceException(joinPoint: JoinPoint, ex: Exception) {
        if (ex is StatusRuntimeException) {
            val meta = Status.trailersFromThrowable(ex)
            val errorResponse = meta?.get(ProtoUtils.keyForProto(ErrorResponse.getDefaultInstance())) as ErrorResponse
            throw GrpcServerException("grpc 서버 예외가 발생하여 예외를 던집니다. 메세지 : ${errorResponse.message}, 예외코드 : ${errorResponse.code}")
        }
    }
}
