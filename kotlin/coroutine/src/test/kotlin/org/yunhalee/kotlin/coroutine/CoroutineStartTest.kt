package org.yunhalee.kotlin.coroutine

import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.concurrent.thread
import kotlin.coroutines.resume
import kotlin.test.Test

class CoroutineStartTest {


    @Test
    fun coroutineStartDefaultTest() {
        runBlocking {
            launch {
                println("CoroutineStartDefaultTest launch start")
            }
            println("CoroutineStartDefaultTest end")
        }
    }

    @Test
    fun coroutineStartWithoutAtomicTest() {
        runBlocking {
            val job = launch {
                println("CoroutineStartWithoutAtomicTest launch start")
            }
            job.cancel()
            println("CoroutineStartWithoutAtomicTest end")
        }
    }

    @Test
    fun coroutineStartWithAtomicTest() {
        runBlocking {
            val job = launch(start = CoroutineStart.ATOMIC) {
                println("CoroutineStartWithoutAtomicTest launch start")
            }
            job.cancel()
            println("CoroutineStartWithoutAtomicTest end")
        }
    }

    @Test
    fun coroutineStartWithUnDispatchedTest() {
        runBlocking {
            launch(start = CoroutineStart.UNDISPATCHED) {
                println("CoroutineStartWithUnDispatchedTest launch start")
                println("launch thread name: ${Thread.currentThread().name}")
            }
            println("CoroutineStartWithUnDispatchedTest end")
            println("thread name: ${Thread.currentThread().name}")
        }
    }

    @Test
    fun coroutineStartWithUnDispatchedWithSuspendTest() {
        runBlocking {
            launch(start = CoroutineStart.UNDISPATCHED) {
                println("CoroutineStartWithUnDispatchedTest launch start")
                println("launch thread name: ${Thread.currentThread().name}")
                delay(1000)
                println("CoroutineStartWithUnDispatchedTest launch end")
                println("launch thread name: ${Thread.currentThread().name}")
            }
            println("CoroutineStartWithUnDispatchedTest end")
            println("thread name: ${Thread.currentThread().name}")
        }
    }

    @Test
    fun coroutineWithUnconfinedDispatcherTest() {
        runBlocking {
            launch(Dispatchers.Unconfined) {
                println("launch start")
                println("launch thread name: ${Thread.currentThread().name}")
            }
        }
        println("end")
        println("thread name: ${Thread.currentThread().name}")
    }

    @Test
    fun coroutineWithUnconfinedDispatcherWithSuspendTest() {
        runBlocking {
            launch(Dispatchers.Unconfined) {
                println("coroutineWithUnconfinedDispatcherTest launch start")
                println("launch thread name: ${Thread.currentThread().name}")
                delay(1000)
                println("coroutineWithUnconfinedDispatcherTest launch end")
                println("launch thread name: ${Thread.currentThread().name}")
            }
            println("coroutineWithUnconfinedDispatcherTest end")
            println("thread name: ${Thread.currentThread().name}")

        }
    }

    @Test
    fun suspendCancellableCoroutineTest() {
        runBlocking {
            println("runBlocking coroutine 일시 중단 호출")
            suspendCancellableCoroutine { continuation: CancellableContinuation<Unit> ->
                println("일시 중단 지점의 runBlocking 코루틴 실행 정보 : ${continuation.context}")
            }
            println("일시 중단된 코루틴이 재개 후 실행 되어야 하는 코드")
        }
    }


    @Test
    fun suspendCancellableCoroutineWithResumeTest() {
        runBlocking {
            println("runBlocking coroutine 일시 중단 호출")
            suspendCancellableCoroutine { continuation: CancellableContinuation<Unit> ->
                println("일시 중단 지점의 runBlocking 코루틴 실행 정보 : ${continuation.context}")
                continuation.resume(Unit)
            }
            println("일시 중단된 코루틴이 재개 후 실행 되어야 하는 코드")
        }
    }

    @Test
    fun suspendCancellableCoroutineWithReturnTest() {
        runBlocking {
            println("runBlocking coroutine 일시 중단 호출")
            val result = suspendCancellableCoroutine { continuation: CancellableContinuation<String> ->
                println("일시 중단 지점의 runBlocking 코루틴 실행 정보 : ${continuation.context}")
                thread {
                    Thread.sleep(1000)
                    continuation.resume("실행 결과")
                }
            }
            println("결과 : $result")
        }
    }

    @Test
    fun test() {
        runBlocking {
            println("runBlocking started")

            val result = suspendCancellableCoroutine<String> { continuation ->
                println("suspending...")
                thread {
                    Thread.sleep(500)
                    continuation.resume("Hello from thread!")
                }
            }

            println("result: $result")
        }
    }
}