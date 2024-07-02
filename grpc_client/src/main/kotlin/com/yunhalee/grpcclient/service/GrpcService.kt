package com.yunhalee.grpcclient.service

import com.yunhalee.Author
import com.yunhalee.BookAuthorServiceGrpc
import com.yunhalee.ErrorResponse
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.grpc.protobuf.ProtoUtils
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.stereotype.Service

@Service
class GrpcService {

    @GrpcClient("grpc-service")
    lateinit var bookAuthorService: BookAuthorServiceGrpc.BookAuthorServiceBlockingStub

    fun getAuthor(): Author? {
        return try {
            val request = Author.newBuilder()
                .setAuthorId(1)
                .setFirstName("Yunha")
                .setLastName("Lee")
                .setGender("Female")
                .setBookId(2)
                .build()
            bookAuthorService.getAuthor(request)
        } catch (ex: StatusRuntimeException) {
            println("Status: ${ex.status}")
            println(ex.trailers)
            println(ex.status)

            val meta = Status.trailersFromThrowable(ex)

            val data = meta?.get(ProtoUtils.keyForProto(ErrorResponse.getDefaultInstance()))
            println(data)
            println(meta)
            println(meta?.keys())
            throw ex

            throw RuntimeException("Failed to get author")
        }
    }
}
