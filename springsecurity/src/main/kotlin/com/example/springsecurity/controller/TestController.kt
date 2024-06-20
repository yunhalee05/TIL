package com.example.springsecurity.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/test")
class TestController : TestSpec {

    @GetMapping
    override fun test() {
        println("test 요청에 성공했습니다. ")
    }
}
