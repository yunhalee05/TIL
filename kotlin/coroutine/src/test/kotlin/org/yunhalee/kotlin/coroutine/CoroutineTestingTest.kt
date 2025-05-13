package org.yunhalee.kotlin.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals

class CoroutineTestingTest {

    @Test
    fun testSuspendFunction() {
        runBlocking {
            val startTime = System.currentTimeMillis()
            val result = suspendFunction(100)
            println("Total time: ${System.currentTimeMillis() - startTime} ms")
            println("Result: $result")
        }
    }

    @Test
    fun testCoroutineSchedulerAdvanceTimeByAndStandardTestDispatcher() {
        val testCoroutineScheduler = TestCoroutineScheduler()
        val testDispatcher = StandardTestDispatcher(testCoroutineScheduler)
        val testCoroutineScope = CoroutineScope(testDispatcher)
        testCoroutineScheduler.advanceTimeBy(1000)
        var result = 0
        testCoroutineScope.launch {
            delay(10000)
            result = 1
            delay(10000)
            result = 2
            println(Thread.currentThread().name)
        }

        assertEquals(0, result)
        testCoroutineScheduler.advanceTimeBy(5000)
        assertEquals(0, result)
        testCoroutineScheduler.advanceTimeBy(6000)
        assertEquals(1, result)
        testCoroutineScheduler.advanceTimeBy(10000)
        assertEquals(2, result)

    }


    @Test
    fun testAdvanceUntilIdle() {
        val testCoroutineScheduler = TestCoroutineScheduler()
        val testDispatcher = StandardTestDispatcher(testCoroutineScheduler)
        val testCoroutineScope = CoroutineScope(testDispatcher)
        testCoroutineScheduler.advanceTimeBy(1000)
        var result = 0
        testCoroutineScope.launch {
            delay(10000)
            result = 1
            delay(10000)
            result = 2
            println(Thread.currentThread().name)
        }

        testCoroutineScheduler.advanceUntilIdle()
        assertEquals(2, result)

    }


    @Test
    fun testStandardTestDispatcher() {
        val testDispatcher = StandardTestDispatcher()
        val testCoroutineScope = CoroutineScope(testDispatcher)
        testDispatcher.scheduler.advanceTimeBy(1000)
        var result = 0
        testCoroutineScope.launch {
            delay(10000)
            result = 1
            delay(10000)
            result = 2
            println(Thread.currentThread().name)
        }

        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(2, result)

    }


    @Test
    fun testTestScope() {
        val testScope = TestScope()
        var result = 0
        testScope.launch {
            delay(10000)
            result = 1
            delay(10000)
            result = 2
            println(Thread.currentThread().name)
        }

        testScope.advanceUntilIdle()
        assertEquals(2, result)

    }


    @Test
    fun testRunTest() {
        var result = 0
        runTest {
            delay(10000)
            result = 1
            delay(10000)
            result = 2
            println(Thread.currentThread().name)
        }

        assertEquals(2, result)

    }

    @Test
    fun testRunTest2() {
        runTest {
            var result = 0

            delay(10000)
            result = 1
            delay(10000)
            result = 2
            println(Thread.currentThread().name)

            assertEquals(2, result)

        }
    }

    @Test
    fun testRunTestWithTestScope() {
        runTest {
            var result = 0

            delay(10000)
            println("가상 시간 : ${this.currentTime}ms")
            result = 1
            delay(10000)
            println("가상 시간 : ${this.currentTime}ms")
            result = 2
            println(Thread.currentThread().name)

            assertEquals(2, result)

        }
    }


    @Test
    fun testRunTestWithAdvanceUntilIdle() {
        runTest {
            var result = 0
            launch {
                delay(10000)
                result = 1
            }

            println("가상 시간 : ${this.currentTime}ms")
            advanceUntilIdle()
            println("가상 시간 : ${this.currentTime}ms")
            println(Thread.currentThread().name)
        }
    }


    @Test
    fun testRunTestWithJoin() {
        runTest {
            var result = 0
            launch {
                delay(10000)
                result = 1
            }.join()

            println("가상 시간 : ${this.currentTime}ms")
            println(Thread.currentThread().name)
        }
    }

    @Test
    fun testRunTestThrow() {
        runTest {
            var result = 0
            launch {
                while (true) {
                    delay(1000)
                    result += 1
                }
            }

            advanceTimeBy(1500)
            assertEquals(1, result)
            advanceTimeBy(1000)
            assertEquals(2, result)
        }
    }

    @Test
    fun backgroundScope() {
        runTest {
            var result = 0
            backgroundScope.launch {
                while (true) {
                    delay(1000)
                    result += 1
                }
            }

            advanceTimeBy(1500)
            assertEquals(1, result)
            advanceTimeBy(1000)
            assertEquals(2, result)
        }

    }


    private suspend fun suspendFunction(repeatTime: Int): Int {
        var result = 0
        repeat(repeatTime) {
            delay(100)
            result += 1
        }
        return result
    }
}