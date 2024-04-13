package com.yunhalee.multithread.controller

import com.yunhalee.multithread.service.ThreadPoolTestService
import com.yunhalee.multithread.util.ThreadBuilder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.CompletableFuture

@RestController("/api/multi-thread")
class MultiThreadTestController(
    private val threadPoolTestService: ThreadPoolTestService
) {

    val maxThreadPoolSize = 100


    @GetMapping("/fixed")
    fun fixedThreadPoolTest(@RequestParam size: Int): String {
        val threadPool = ThreadBuilder.fixed(maxThreadPoolSize)
        val list = (0 .. size).toList()

        threadPool.submit {
            list.forEach {
                CompletableFuture.runAsync {
                    threadPoolTestService.test(it)
                }
            }
        }
        return "test executed"
    }


    @GetMapping("/cached")
    fun cachedThreadPoolTest(@RequestParam size: Int): String {
        val threadPool = ThreadBuilder.cached(minThreadPoolSize = maxThreadPoolSize)
        val list = (0 .. size).toList()

        threadPool.submit {
            list.forEach {
                CompletableFuture.runAsync {
                    threadPoolTestService.test(it)
                }
            }
        }
        return "test executed"
    }
}