package com.yunhalee.transaction.transaction.controller


import com.yunhalee.transaction.transaction.service.ParentService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TransactionTestController(
    private val parentService: ParentService
) {

    @GetMapping("/test")
    fun test(): String {
        parentService.callFirstChild()
        return "test executed"
    }
}