package org.yunhalee.kotlin.coroutine

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope
import org.junit.jupiter.api.Test

class SuspendFunctionTest {


    @Test
    fun suspendFunction() {
        runBlocking {
            delayAndPrint()
            delayAndPrint()
            // 2초 소요됨 (runBlocking이 블로킹 방식으로 실행되기 때문)
        }
    }

    @Test
    fun suspendFunctionWithCoroutineBuilder() {
        runBlocking {
            val startTime = System.currentTimeMillis()
            launch {
                delayAndPrint()
            }
            launch {
                delayAndPrint()
            }
            println("Total time: ${System.currentTimeMillis() - startTime} ms")
        }
//        Total time: 4 ms
//        Hello World!
//        Hello World!
        // 총 소요시간 약 1초 (launch가 비동기 방식으로 실행되기 때문)
    }

    @Test
    fun suspendFunctionCanCallOtherSuspendFunction() {
        runBlocking {
            val startTime = System.currentTimeMillis()
            suspendFunctionCallingOtherSuspendFunction()
            println("Total time: ${System.currentTimeMillis() - startTime} ms")
        }
    }

    private suspend fun suspendFunctionCallingOtherSuspendFunction() {
        println("Hello from suspend function!")
        delayAndPrint()
    }


    @Test
    fun suspendFunctionCanCallOtherSuspendFunction2() {
        runBlocking {
            val startTime = System.currentTimeMillis()
            suspendFunctionCallingOtherSuspendFunction2()
            println("Total time: ${System.currentTimeMillis() - startTime} ms")
        }
    }

    private suspend fun suspendFunctionCallingOtherSuspendFunction2() {
        println("Hello from suspend function!")
        delayAndPrint()
        delayAndPrint()
    }


    @Test
    fun suspendFunctionCanCallOtherSuspendFunction3() {
        runBlocking {
            val startTime = System.currentTimeMillis()
            suspendFunctionCallingOtherSuspendFunction3()
            println("Total time: ${System.currentTimeMillis() - startTime} ms")
        }
    }

    private suspend fun suspendFunctionCallingOtherSuspendFunction3() = coroutineScope {
        println("Hello from suspend function!")
        async {
            delayAndPrint()
        }
        async {
            delayAndPrint()
        }
    }

    private suspend fun delayAndPrint() {
        delay(1000)
        println("Hello World!")
    }

    @Test
    fun suspendFunctionCanCallOtherSuspendFunction4() {
        runBlocking {
            val startTime = System.currentTimeMillis()
            suspendFunctionCallingOtherSuspendFunction4()
            println("Total time: ${System.currentTimeMillis() - startTime} ms")
        }
    }

    private suspend fun suspendFunctionCallingOtherSuspendFunction4() = supervisorScope {
        println("Hello from suspend function!")
        val deferred1 = async {
            throw Exception("Error in async block")
            delayAndPrint2()
        }
        val deferred2 = async {
            delayAndPrint2()
        }
        val result1 = try {
            deferred1.await()
        } catch (e: Exception) {
            arrayOf()
        }

        val result2 = try {
            deferred2.await()
        } catch (e: Exception) {
            arrayOf()
        }
        println(arrayOf(*result1, *result2).joinToString(", "))
    }


    private suspend fun delayAndPrint2(): Array<String> {
        delay(1000)
        return arrayOf("1", "2", "3")
    }


    @Test
    fun testExample1() {
        runBlocking {
            val start = System.currentTimeMillis()
            try {
                launchParallelTasksWithCoroutineScope()
            } catch (e: Exception) {
                println("Caught in runBlocking: ${e.message}")
            }
            println("Total time: ${System.currentTimeMillis() - start} ms")
        }
    }

    private suspend fun launchParallelTasksWithCoroutineScope() =
        coroutineScope {
            println("Start suspend function")

            val deferred1 = async {
                delay(500)
                println("Deferred1 done")
                "Result1"
            }

            val deferred2 = async {
                delay(300)
                println("Deferred2 throwing...")
                throw IllegalStateException("Failure in deferred2")
            }

            val result1 = try {
                deferred1.await()
            } catch (e: Exception) {
                println("Caught deferred1 error: ${e.message}")
                null
            }

            val result2 = try {
                deferred2.await()
            } catch (e: Exception) {
                println("Caught deferred2 error: ${e.message}")
                null
            }

            println("Final result: $result1, $result2")
        }


    @Test
    fun testExample2() {
        runBlocking {
            val start = System.currentTimeMillis()
            try {
                launchParallelTasksWithSupervisorScope()
            } catch (e: Exception) {
                println("Caught in runBlocking: ${e.message}")
            }
            println("Total time: ${System.currentTimeMillis() - start} ms")
        }
    }

    private suspend fun launchParallelTasksWithSupervisorScope() =
        supervisorScope {
            println("Start suspend function")

            val deferred1 = async {
                delay(500)
                println("Deferred1 done")
                "Result1"
            }

            val deferred2 = async {
                delay(300)
                println("Deferred2 throwing...")
                throw IllegalStateException("Failure in deferred2")
            }

            val result1 = try {
                deferred1.await()
            } catch (e: Exception) {
                println("Caught deferred1 error: ${e.message}")
                null
            }

            val result2 = try {
                deferred2.await()
            } catch (e: Exception) {
                println("Caught deferred2 error: ${e.message}")
                null
            }

            println("Final result: $result1, $result2")
        }


}