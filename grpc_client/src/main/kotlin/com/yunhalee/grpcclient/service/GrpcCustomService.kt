package com.yunhalee.grpcclient.service

import com.yunhalee.Author
import com.yunhalee.BookAuthorServiceGrpc
import com.yunhalee.grpcclient.support.annotation.GrpcComponent
import io.grpc.Channel
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.stereotype.Service

@Service
@GrpcComponent
class GrpcCustomService(
    val channel: Channel
) {

    private val bookAuthorService: BookAuthorServiceGrpc.BookAuthorServiceBlockingStub by lazy {
        BookAuthorServiceGrpc.newBlockingStub(channel)
    }

    fun getAuthor(): Author? {
        val request = Author.newBuilder()
            .setAuthorId(2)
            .setFirstName("Yunha")
            .setLastName("Lee")
            .setGender("Female")
            .setBookId(2)
            .build()
        return try {
            bookAuthorService.getAuthor(request)
        }catch (ex: Exception){
            println("try catch 문으로 예외를 잡았습니다.: $ex")
            throw ex
        }
//        } catch (ex: StatusRuntimeException) {
//            println("Status: ${ex.status}")
//            println(ex.trailers)
//            println(ex.status)
//
//            val meta = Status.trailersFromThrowable(ex)
//
//            val data = meta?.get(ProtoUtils.keyForProto(ErrorResponse.getDefaultInstance()))
//            println(data)
//            println(meta)
//            println(meta?.keys())
//            throw ex
//
//            throw RuntimeException("Failed to get author")
//        }
    }
}
