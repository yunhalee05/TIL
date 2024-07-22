package com.example.springsecurity.grpc

import com.yunhalee.Author
import com.yunhalee.Book
import com.yunhalee.BookAuthorServiceGrpcKt
import kotlinx.coroutines.flow.Flow
import net.devh.boot.grpc.server.service.GrpcService

@GrpcService
class BookGrpcService: BookAuthorServiceGrpcKt.BookAuthorServiceCoroutineImplBase() {
    override suspend fun getAuthor(request: Author): Author {
        return Author.newBuilder()
            .setAuthorId(1)
            .setBookId(1)
            .build()
    }

}