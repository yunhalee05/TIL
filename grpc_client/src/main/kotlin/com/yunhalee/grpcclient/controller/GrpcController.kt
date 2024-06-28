package com.yunhalee.grpcclient.controller

import com.yunhalee.grpcclient.service.GrpcService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class GrpcController(
    private val grpcService: GrpcService
) {

    @GetMapping("/grpc-test")
    fun grpcTest() {
        grpcService.getAuthor()
        return
    }
}
