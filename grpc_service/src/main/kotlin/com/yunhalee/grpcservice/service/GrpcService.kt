package com.yunhalee.grpcservice.service

import com.yunhalee.Author
import com.yunhalee.Book
import com.yunhalee.BookAuthorServiceGrpcKt
import net.devh.boot.grpc.server.service.GrpcService

@GrpcService
class GrpcService : BookAuthorServiceGrpcKt.BookAuthorServiceCoroutineImplBase() {

    override suspend fun getAuthor(request: Author): Author {
        if (request.authorId == 1) {
            return Author.newBuilder()
                .setAuthorId(1)
                .setBookId(1)
                .build()
        }
        throw RuntimeException("작가 id는 1이어야 합니다.")
    }

    override suspend fun getExpensiveBook(requests: kotlinx.coroutines.flow.Flow<Book>): Book {
        return super.getExpensiveBook(requests)
    }
}
