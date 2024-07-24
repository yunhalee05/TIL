package com.example.springsecurity.grpc

import com.example.springsecurity.config.security.authentication.MultiAuthentications
import com.yunhalee.Author
import com.yunhalee.Book
import com.yunhalee.BookAuthorServiceGrpcKt
import io.grpc.Context
import kotlinx.coroutines.flow.Flow
import net.devh.boot.grpc.server.security.interceptors.AuthenticatingServerInterceptor
import net.devh.boot.grpc.server.service.GrpcService

@GrpcService
class BookGrpcService: BookAuthorServiceGrpcKt.BookAuthorServiceCoroutineImplBase() {
    override suspend fun getAuthor(request: Author): Author {
        val authentication = AuthenticatingServerInterceptor.AUTHENTICATION_CONTEXT_KEY.get() as MultiAuthentications
        println(authentication.authentications.size)
        authentication.authentications.forEach {
            println(it.tokenUser)
            println(it.authorities)
        }
        return Author.newBuilder()
            .setAuthorId(1)
            .setBookId(1)
            .build()
    }

}