package com.yunhalee.grpcclient.component

import com.yunhalee.ErrorResponse
import com.yunhalee.grpcclient.config.exception.mapper.GrpcErrorResponseMapperToDto
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

    // AOP는 함수의 try catch 문까지 모두 실행된 후에 실행이 됨
    @AfterThrowing(pointcut = "grpcClientService()", throwing = "ex")
    fun handleGrpcClientServiceException(joinPoint: JoinPoint, ex: Exception) {
        if (ex is StatusRuntimeException) {
            val meta = Status.trailersFromThrowable(ex)
            val errorResponse = meta?.get(ProtoUtils.keyForProto(ErrorResponse.getDefaultInstance())) as ErrorResponse
            val grpcErrorResponse = GrpcErrorResponseMapperToDto.mapToDto(errorResponse)
//            throw GrpcServerException("grpc 서버 예외가 발생하여 예외를 던집니다. 메세지 : ${errorResponse.message}, 예외코드 : ${errorResponse.code}")
            throw GrpcServerException("grpc 서버 예외가 발생하여 예외를 던집니다. ", grpcErrorResponse, ex)
        }
    }
}
