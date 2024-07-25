package com.yunhalee.grpcclient.config.exception.mapper

import com.yunhalee.ErrorResponse
import com.yunhalee.grpcclient.config.exception.GrpcErrorResponse
import org.mapstruct.CollectionMappingStrategy
import org.mapstruct.Mapper
import org.mapstruct.NullValueCheckStrategy
import org.mapstruct.ReportingPolicy
import org.mapstruct.factory.Mappers

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
)
interface GrpcErrorResponseMapper {
    fun mapToProto(dto: GrpcErrorResponse): ErrorResponse

    companion object {
        private val INSTANCE = Mappers.getMapper(GrpcErrorResponseMapper::class.java)

        fun GrpcErrorResponse.mapToProto(): ErrorResponse = INSTANCE.mapToProto(this)
    }
}

class GrpcErrorResponseMapperToDto {
    companion object {
        fun mapToDto(proto: ErrorResponse): GrpcErrorResponse {
            return GrpcErrorResponse(
                message = proto.message,
                code = proto.code,
            )
        }
    }
}
