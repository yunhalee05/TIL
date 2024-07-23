package com.example.springsecurity.controller

import com.example.springsecurity.config.security.authentication.TokenMember
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/test")
class TestController : TestSpec {

    @GetMapping
    // resolver에서 사용된 role은 다음과 같이 제한하여 놓치지 않게 한다.
    @PreAuthorize("hasRole(T(com.example.springsecurity.config.security.Role).MEMBER)")
    override fun test(member: TokenMember) {
        println(member.age)
        println("test 요청에 성공했습니다. ")
    }
}
