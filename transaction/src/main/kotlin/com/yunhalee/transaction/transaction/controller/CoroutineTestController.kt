package com.yunhalee.transaction.transaction.controller

import com.yunhalee.transaction.transaction.service.ParentService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController("/api/coroutine")
class CoroutineTestController(
    private val parentService: ParentService
) {

    @GetMapping
    fun semaphoreCoroutine(): String {
        parentService.callFirstChild()
        return "test executed"
    }

}