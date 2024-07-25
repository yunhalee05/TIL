package com.yunhalee.grpcclient.config.exception

data class GrpcErrorResponse(
    val code: String,
    val message: String
) {
    companion object {
        fun of(code: String, message: String): GrpcErrorResponse {
            return GrpcErrorResponse(code, message)
        }
    }

}