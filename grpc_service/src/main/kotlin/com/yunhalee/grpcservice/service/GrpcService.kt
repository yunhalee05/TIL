package com.yunhalee.grpcservice.service


import com.yunhalee.Author
import com.yunhalee.Book
import com.yunhalee.BookAuthorServiceGrpcKt
import net.devh.boot.grpc.server.service.GrpcService


@GrpcService
class GrpcService: BookAuthorServiceGrpcKt.BookAuthorServiceCoroutineImplBase() {

    override suspend fun getAuthor(request: Author): Author {
        throw RuntimeException("Not implemented")
        return super.getAuthor(request)
    }

    override suspend fun getExpensiveBook(requests: kotlinx.coroutines.flow.Flow<Book>): Book {
        return super.getExpensiveBook(requests)
    }

}