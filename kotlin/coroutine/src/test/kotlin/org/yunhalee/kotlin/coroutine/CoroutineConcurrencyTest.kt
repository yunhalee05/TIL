package org.yunhalee.kotlin.coroutine

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.Nested
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent.locks.ReentrantLock
import kotlin.test.Test

class CoroutineConcurrencyTest {


    @Nested
    inner class ConcurrencyProblemTest() {

        private var count = 0 // 공유 자원

        @Test
        fun concurrencyTest() {
            runBlocking {
                withContext(Dispatchers.Default) {
                    repeat(10000) {
                        launch {
                            count++
                        }
                    }
                }
            }
            println(count)
        }
    }


    @Nested
    inner class ConcurrencyProblemWithVolatileTest() {

        @Volatile
        private var count = 0 // 공유 자원

        @Test
        fun concurrencyTest() {
            runBlocking {
                withContext(Dispatchers.Default) {
                    repeat(10000) {
                        launch {
                            count++
                        }
                    }
                }
            }
            println(count)
        }
    }


    @Nested
    inner class ConcurrencyProblemWithMutexTest() {

        private var count = 0 // 공유 자원
        private val mutex = Mutex()

        @Test
        fun concurrencyTest() {
            runBlocking {
                withContext(Dispatchers.Default) {
                    repeat(10000) {
                        launch {
                            mutex.lock()
                            count++
                            mutex.unlock()
                        }
                    }
                }
            }
            println(count)
        }

        @Test
        fun concurrencyTest2() {
            runBlocking {
                withContext(Dispatchers.Default) {
                    repeat(10000) {
                        launch {
                            mutex.withLock {
                                count++
                            }
                        }
                    }
                }
            }
            println(count)
        }
    }


    @Nested
    inner class ConcurrencyProblemWithReentrantLockTest() {

        private var count = 0 // 공유 자원
        private val reentrantLock = ReentrantLock()

        @Test
        fun concurrencyTest() {
            runBlocking {
                withContext(Dispatchers.Default) {
                    repeat(10000) {
                        launch {
                            reentrantLock.lock()
                            count++
                            reentrantLock.unlock()
                        }
                    }
                }
            }
            println(count)
        }
    }


    @Nested
    inner class ConcurrencyProblemWithSingleThreadContextTest() {

        private var count = 0 // 공유 자원
        private val countChangeDispatcher = newSingleThreadContext("CountChangeThread")

        @Test
        fun concurrencyTest() {
            runBlocking {
                withContext(Dispatchers.Default) {
                    repeat(10000) {
                        launch {
                            increaseCount()
                        }
                    }
                }
            }
            println(count)
        }

        private suspend fun increaseCount() {
            withContext(countChangeDispatcher) {
                count++
            }
        }
    }


    @Nested
    inner class ConcurrencyProblemWithAtomicTest() {

        private var count = AtomicInteger(0) // 공유 자원

        @Test
        fun concurrencyTest() {
            runBlocking {
                withContext(Dispatchers.Default) {
                    repeat(10000) {
                        launch {
                            count.getAndUpdate {
                                it + 1
                            }
                        }
                    }
                }
            }
            println(count)
        }

        @Test
        fun concurrencyTest2() {
            runBlocking {
                withContext(Dispatchers.Default) {
                    repeat(10000) {
                        launch {
                            val cur = count.get()
                            count.set(cur + 1)
                        }
                    }
                }
            }
            println(count)
        }


        @Test
        fun concurrencyTest3() {
            runBlocking {
                withContext(Dispatchers.Default) {
                    repeat(10000) {
                        launch {
                            count.incrementAndGet()
                        }
                    }
                }
            }
            println(count)
        }
    }


    data class Counter(val name: String, val count: Int) // 공유 자원

    @Nested
    inner class ConcurrencyProblemWithAtomicReferenceTest() {

        val atomicCounter = AtomicReference(Counter("Counter", 0))

        @Test
        fun concurrencyTest() {
            runBlocking {
                withContext(Dispatchers.Default) {
                    repeat(10000) {
                        launch {
                            atomicCounter.getAndUpdate {
                                it.copy(count = it.count + 1)
                            }
                        }
                    }
                }
            }
            println(atomicCounter.get())
        }
    }






}