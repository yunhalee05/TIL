package kr.co.yunhalee.transaction.repository.controller

import kr.co.yunhalee.transaction.service.ParentService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller(
    private val parentService: ParentService
) {

    @GetMapping("/test")
    fun test(): String {
        parentService.callFirstChild()
        return "test executed"
    }
}