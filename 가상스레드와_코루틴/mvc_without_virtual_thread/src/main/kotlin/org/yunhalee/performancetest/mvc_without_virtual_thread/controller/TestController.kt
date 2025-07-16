package org.yunhalee.performancetest.mvc_without_virtual_thread.controller

import kotlinx.coroutines.delay
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {

    @GetMapping("/test")
    suspend fun test(): String {
        println("=======test start=======: ${Thread.currentThread().name}")
        suspendDelay()
        println("=======test end=======: ${Thread.currentThread().name}")
        return "Test completed"
    }

    suspend fun suspendDelay() {
        println("=======delay start=======: ${Thread.currentThread().name}")
        delay(3000)
        println("=======delay end=======: ${Thread.currentThread().name}")
    }


}