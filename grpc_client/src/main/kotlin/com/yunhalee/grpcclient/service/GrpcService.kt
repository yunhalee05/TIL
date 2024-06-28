package com.yunhalee.grpcclient.service

import com.yunhalee.Author
import com.yunhalee.BookAuthorServiceGrpc
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.stereotype.Service

@Service
class GrpcService {

    @GrpcClient("grpc-service")
    lateinit var bookAuthorService: BookAuthorServiceGrpc.BookAuthorServiceBlockingStub

    fun getAuthor() {
        val request = Author.newBuilder()
            .setAuthorId(1)
            .setFirstName("Yunha")
            .setLastName("Lee")
            .setGender("Female")
            .setBookId(2)
            .build()
        val author = bookAuthorService.getAuthor(request)
        println(author)
    }
}
