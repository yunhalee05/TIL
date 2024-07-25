package com.yunhalee.grpcclient.config

import io.grpc.CallOptions
import io.grpc.Channel
import io.grpc.ClientCall
import io.grpc.ClientInterceptor
import io.grpc.ForwardingClientCall
import io.grpc.Metadata
import io.grpc.MethodDescriptor
import io.grpc.Status

open class GrpcClientHeaderInterceptor(private val requestHeaders: Map<String, String>) : ClientInterceptor {
    override fun <ReqT : Any, RespT : Any> interceptCall(
        method: MethodDescriptor<ReqT, RespT>,
        callOptions: CallOptions,
        next: Channel,
    ): ClientCall<ReqT, RespT> {
        val clientCall = next.newCall(method, callOptions)
        return object : ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(clientCall) {
            override fun start(
                responseListener: Listener<RespT>,
                headers: Metadata,
            ) {
                requestHeaders.forEach {
                    if (it.key == "Authorization") {
                        headers.put(Metadata.Key.of(it.key, Metadata.ASCII_STRING_MARSHALLER), "Bearer ${it.value}")
                    } else {
                        headers.put(Metadata.Key.of(it.key, Metadata.ASCII_STRING_MARSHALLER), it.value)
                    }
                }
                super.start(responseListener, headers)
            }
        }
    }
}