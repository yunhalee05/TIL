package com.yunhalee.grpcclient.controller

import com.yunhalee.Author
import com.yunhalee.grpcclient.service.GrpcCustomService
import com.yunhalee.grpcclient.service.GrpcService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class GrpcController(
    private val grpcService: GrpcService,
    private val grpcCustomService: GrpcCustomService
) {

    @GetMapping("/grpc-test")
    fun grpcTest(): Author? {
        grpcService.getAuthor()
        return null
    }

    @GetMapping("/grpc-custom-test")
    fun grpcCustomTest(): Author? {
        grpcCustomService.getAuthor()
        return null
    }
}
