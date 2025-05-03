package org.yunhalee.performancetest.server.grpc

import com.yunhalee.performance_test.Author
import com.yunhalee.performance_test.Book
import com.yunhalee.performance_test.BookAuthorServiceGrpcKt
import kotlinx.coroutines.delay
import net.devh.boot.grpc.server.service.GrpcService

@GrpcService
class GrpcServer : BookAuthorServiceGrpcKt.BookAuthorServiceCoroutineImplBase() {

    override suspend fun getAuthor(request: Author): Author {
        if (request.authorId % 13 == 0) {
            return Author.newBuilder()
                .setAuthorId(request.authorId)
                .setBookId(1)
                .build()
        }
        delay(100)
        throw RuntimeException("작가 id는 13의 배수이어야 합니다.")
    }

    override suspend fun getExpensiveBook(requests: kotlinx.coroutines.flow.Flow<Book>): Book {
        return super.getExpensiveBook(requests)
    }
}