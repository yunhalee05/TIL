package com.yunhalee.grpcclient.config

import io.grpc.CallOptions
import io.grpc.Channel
import io.grpc.ClientCall
import io.grpc.ClientInterceptor
import io.grpc.Metadata
import io.grpc.MethodDescriptor
import io.grpc.Status

class GrpcClientInterceptor : ClientInterceptor {

    private val logger = org.slf4j.LoggerFactory.getLogger(GrpcClientInterceptor::class.java)

    override fun <ReqT, RespT> interceptCall(
        method: MethodDescriptor<ReqT, RespT>,
        callOptions: CallOptions,
        next: Channel
    ): ClientCall<ReqT, RespT> {
        return object : ClientCall<ReqT, RespT>() {

            private val delegate = next.newCall(method, callOptions)

            override fun start(responseListener: Listener<RespT>, headers: io.grpc.Metadata) {
                delegate.start(object : Listener<RespT>() {
                    override fun onHeaders(headers: io.grpc.Metadata) {
                        responseListener.onHeaders(headers)
                    }

                    override fun onMessage(message: RespT) {
                        responseListener.onMessage(message)
                    }

                    override fun onClose(status: Status, trailers: Metadata) {
                        if (status.code != Status.Code.OK) {
                            logger.info("Trailers received from server: $trailers")
                        }
                        responseListener.onClose(status, trailers)
                    }

                    override fun onReady() {
                        responseListener.onReady()
                    }
                }, headers)
            }

            override fun request(numMessages: Int) {
                delegate.request(numMessages)
            }

            override fun cancel(message: String?, cause: Throwable?) {
                delegate.cancel(message, cause)
            }

            override fun halfClose() {
                delegate.halfClose()
            }

            override fun sendMessage(message: ReqT) {
                delegate.sendMessage(message)
            }

        }
    }
}
