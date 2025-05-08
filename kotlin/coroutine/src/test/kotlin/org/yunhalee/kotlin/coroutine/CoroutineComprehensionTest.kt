package org.yunhalee.kotlin.coroutine

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.jupiter.api.Test

class CoroutineComprehensionTest {


    @Test
    fun joinAndAwaitMethodYieldThread() {
        runBlocking {
            val startTime = System.currentTimeMillis()
            val job = launch {
                println("launch job started")
                delay(1000)
                println("launch job finished")
            }
            println("runBlocking job will be suspended and main thread will be yielded")
            job.join() // join()을 사용하여 작업이 완료될 때까지 대기
            println("runBlocking job finished")
            println("Total time: ${System.currentTimeMillis() - startTime} ms")
        }
    }

    @Test
    fun suspendedCoroutineCanBeAllocatedToDifferentThread() {
        runBlocking {
            val dispatcher = newFixedThreadPoolContext(2, "TestThread")
            val startTime = System.currentTimeMillis()
            launch(dispatcher) {
                repeat(5) {
                    println("Thread name: ${Thread.currentThread().name} 코루틴 실행이 일시 중단 됨")
                    delay(100)
                    println("Thread name: ${Thread.currentThread().name} 코루틴 실행이 재개 됨")
                }
            }
            println("${System.currentTimeMillis() - startTime} ms이 소요됨")
        }
    }

    @Test
    fun suspendedButNotYeildCoroutineCannotBeAllocatedToDifferentThread() {
        runBlocking {
            val dispatcher = newFixedThreadPoolContext(2, "TestThread")
            val startTime = System.currentTimeMillis()
            launch(dispatcher) {
                repeat(5) {
                    println("Thread name: ${Thread.currentThread().name} 코루틴 실행이 일시 중단 됨")
                    Thread.sleep(100)
                    println("Thread name: ${Thread.currentThread().name} 코루틴 실행이 재개 됨")
                }
            }
            println("${System.currentTimeMillis() - startTime} ms이 소요됨")
        }
    }

    @Test
    fun asyncAwaitThreadYieldTest() = runBlocking {
        val dispatcher = newSingleThreadContext("SingleThread")
        val start = System.currentTimeMillis()

        println("[${Thread.currentThread().name}] runBlocking started")

        val deferred = async(dispatcher) {
            println("[${Thread.currentThread().name}] async started")
            delay(300)
            println("[${Thread.currentThread().name}] async finished")
            "done"
        }

        println("[${Thread.currentThread().name}] before await")
        val result = deferred.await()
        println("[${Thread.currentThread().name}] after await: $result")

        println("Total time: ${System.currentTimeMillis() - start} ms")
    }


}