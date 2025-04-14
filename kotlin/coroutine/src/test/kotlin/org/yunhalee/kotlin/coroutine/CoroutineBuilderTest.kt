package org.yunhalee.kotlin.coroutine


import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield
import org.junit.jupiter.api.Test

class CoroutineBuilderTest {

    @Test
    fun launch() {
        // 결과 없이 실행
        runBlocking {
            launch {
                println("launch: Hello from coroutine!")
            }
            println("launch: Hello from main")
        }
    }

    @Test
    fun async() {
        // 결과를 반환하는 비동기 작업
        runBlocking {
            val deferred = async {
                println("async: Hello from coroutine!")
                100
            }
            println("async: Result = ${deferred.await()}")
        }
    }

    @Test
    fun runBlocking() {
        // 블로킹 방식으로 실행
        runBlocking {
            println("runBlocking: Hello from coroutine!")
        }
        println("runBlocking: Hello from main")
    }

    @Test
    fun join() {
        // 비동기 작업이 완료될 때까지 대기
        runBlocking {
            var a = 100
            val job = launch {
                println("join: Hello from coroutine!")
                a = 200
            }
            // JobSupport 구현체
            job.join() // join()을 사용하여 작업이 완료될 때까지 대기
            println("join: Result = $a") // 200
            println("join: Hello from main")
        }
    }

    @Test
    fun joinAll() {
        // 여러 비동기 작업이 완료될 때까지 대기
        runBlocking {
            val job1 = launch {
                println("joinAll: Hello from coroutine 1!")
            }
            val job2 = launch {
                println("joinAll: Hello from coroutine 2!")
            }
            kotlinx.coroutines.joinAll(job1, job2) // joinAll()을 사용하여 모든 작업이 완료될 때까지 대기
            println("joinAll: Hello from main")
        }
    }

    @Test
    fun coroutineStartLazy() {
        // 코루틴을 지연 실행
        runBlocking {
            val startTime = System.currentTimeMillis()
            val job = launch(start = kotlinx.coroutines.CoroutineStart.LAZY) {
                println("Coroutine started after ${System.currentTimeMillis() - startTime} ms")
            }
            println("Before starting coroutine")
            delay(1000)
            job.start() // 코루틴 시작
            job.join()
            println("After starting coroutine")
        }
//        Before starting coroutine
//        Coroutine started after 1008 ms
//        After starting coroutine
    }

    @Test
    fun cancel() {
        // 코루틴 취소
        runBlocking {
            val job = launch {
                repeat(1000) { i ->
                    println("Coroutine is working: $i")
                    delay(100)
                }
            }
            delay(500)
            job.cancel() // 코루틴 취소
            println("Coroutine cancelled")
        }
//        Coroutine is working: 0
//        Coroutine is working: 1
//        Coroutine is working: 2
//        Coroutine is working: 3
//        Coroutine is working: 4
//        Coroutine cancelled
    }

    @Test
    fun cancelAndJoin() {
        // 코루틴 취소 및 대기
        runBlocking {
            val job = launch {
                repeat(1000) { i ->
                    println("Coroutine is working: $i")
                    delay(100)
                }
            }
            delay(500)
            job.cancelAndJoin() // 코루틴 취소 및 대기
            launch {
                println("after cancelAndJoin")
            }
            println("Coroutine cancelled and joined")
        }
    }

    @Test
    fun cancelWithNoCancellable() {
        // 취소 불가능한 코루틴
        runBlocking {
            val job = launch {
               while (true) {
                   println("Coroutine is working")
               }
            }
            delay(100)
            job.cancel() // 코루틴 취소
            println("Coroutine cancelled")
        }
    }

    @Test
    fun cancelWithDelay() {
        // 취소 가능한 코루틴
        runBlocking {
            val job = launch {
                while (true) {
                    println("Coroutine is working")
                    delay(20)
                }
            }
            delay(100)
            job.cancel() // 코루틴 취소
            println("Coroutine cancelled")
        }
    }

    @Test
    fun cancelWithYield() {
        // 취소 가능한 코루틴
        runBlocking {
            val job = launch {
                while (true) {
                    println("Coroutine is working")
                    yield()

                }
            }
            delay(100)
            job.cancel() // 코루틴 취소
            println("Coroutine cancelled")
        }
    }

    @Test
    fun cancelWithCoroutineScopeIsActive() {
        // 코루틴 스코프가 활성 상태인지 확인
        runBlocking {
            val job = launch {
                while (this.isActive) {
                    println("Coroutine is working")
                    delay(20)
                }
            }
            delay(100)
            job.cancel() // 코루틴 취소
            println("Coroutine cancelled")
        }
    }
}