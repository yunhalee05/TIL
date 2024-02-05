package kr.co.yunhalee.study.coroutine.controller

import kr.co.yunhalee.study.transaction.service.ParentService
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