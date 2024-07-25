package com.yunhalee.grpcclient.exception

import com.yunhalee.grpcclient.config.exception.GrpcErrorResponse

class GrpcServerException(message: String, errorResponse: GrpcErrorResponse, throwable: Throwable) : RuntimeException(message + errorResponse.toString(), throwable)
