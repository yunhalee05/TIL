package org.yunhalee.kotlin.coroutine


import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import org.junit.jupiter.api.Test

class CoroutineAsyncTest {

    @Test
    fun async() {
        runBlocking {
            val result: Deferred<String> = async {
                delay(1000)
                "async: Hello from coroutine!"
            }
        }
    }

    @Test
    fun join() {
        runBlocking {
            val job = launch {
                println("join: Hello from coroutine!")
                delay(1000)
            }
            job.join() // join()을 사용하여 작업이 완료될 때까지 대기
            println("join: Hello from main")
        }
    }


    @Test
    fun await() {
        runBlocking {
            val startTime = System.currentTimeMillis()
            val deferred1 = async {
                delay(1000)
                "async1: Hello from coroutine!"
            }
            val deferred2 = async {
                delay(1000)
                "async2: Hello from coroutine!"
            }
            val result1 = deferred1.await()
            val result2 = deferred2.await()
            val endTime = System.currentTimeMillis()
            println("await: Result1 = $result1")
            println("await: Result2 = $result2")
            println("await: Time taken = ${endTime - startTime} ms")
        }
    }

    @Test
    fun awaitSuspend() {
        runBlocking {
            val startTime = System.currentTimeMillis()
            val deferred1 = async {
                delay(1000)
                "async: Hello from coroutine1!"
            }
            val result1 = deferred1.await()

            val deferred2 = async {
                delay(1000)
                "async: Hello from coroutine2!"
            }

            val result2 = deferred2.await()
            val endTime = System.currentTimeMillis()
            println("awaitSuspend: Result1 = $result1")
            println("awaitSuspend: Result2 = $result2")
            println("awaitSuspend: Time taken = ${endTime - startTime} ms")
        }
    }

    @Test
    fun awaitAll() {
        runBlocking {
            val deferred1 = async {
                delay(1000)
                "async: Hello from coroutine1!"
            }
            val deferred2 = async {
                delay(1000)
                "async: Hello from coroutine2!"
            }
            val results = kotlinx.coroutines.awaitAll(deferred1, deferred2)
            println("awaitAll: Results = $results")
        }
    }

    @Test
    fun listAwaitAll() {
        runBlocking {
            val deferred1 = async {
                delay(1000)
                "async: Hello from coroutine1!"
            }
            val deferred2 = async {
                delay(1000)
                "async: Hello from coroutine2!"
            }
            val results = listOf(deferred1, deferred2).awaitAll()
            println("awaitAll: Results = $results")
        }
    }


    @Test
    fun withContext() {
        runBlocking {
            val result: String = kotlinx.coroutines.withContext(Dispatchers.IO) {
                delay(1000)
                "withContext: Hello from coroutine!"
            }
            println(result)
        }
    }

    @Test
    fun asyncAwaitThread() {
        runBlocking {
            println("mainThread: Hello from main thread: ${Thread.currentThread().name}")
            val startTime = System.currentTimeMillis()
            val deferred1 = async(Dispatchers.Default) {
                println("asyncAwaitThread: Coroutine 1 is running on thread: ${Thread.currentThread().name}")
                delay(1000)
                "Result from Coroutine 1"
            }

            val deferred2 = async(Dispatchers.IO) {
                println("asyncAwaitThread: Coroutine 2 is running on thread: ${Thread.currentThread().name}")
                delay(1000)
                "Result from Coroutine 2"
            }

            val result1 = deferred1.await()
            val result2 = deferred2.await()

            val endTime = System.currentTimeMillis()
            println("asyncAwaitThread: Results: $result1, $result2")
            println("asyncAwaitThread: Time taken = ${endTime - startTime} ms")
        }

    }

    @Test
    fun withContextThread() {
        runBlocking {
            println("mainThread: Hello from main thread: ${Thread.currentThread().name}")
            val startTime = System.currentTimeMillis()
            val result1 = withContext(Dispatchers.IO) {
                println("withContextThread: Coroutine 1 is running on thread: ${Thread.currentThread().name}")
                delay(1000)
                "Result from Coroutine 1"
            }

            val result2 = withContext(Dispatchers.IO) {
                println("withContextThread: Coroutine 2 is running on thread: ${Thread.currentThread().name}")
                delay(1000)
                "Result from Coroutine 2"
            }

            val endTime = System.currentTimeMillis()
            println("withContextThread: Results: $result1, $result2")
            println("withContextThread: Time taken = ${endTime - startTime} ms")
        }
    }

    @Test
    fun withContextSwitch() {
        runBlocking {
            println("mainThread: Hello from main thread: ${Thread.currentThread().name}")
            val dispatcher1 = newSingleThreadContext("Coroutine1Thread")
            val dispatcher2 = newSingleThreadContext("Coroutine2Thread")
            val startTime = System.currentTimeMillis()
            withContext(dispatcher1) {
                println("withContextSwitch: Coroutine 1 is running on thread: ${Thread.currentThread().name}")
                delay(1000)
                withContext(dispatcher2) {
                    println("withContextSwitch: Coroutine 2 is running on thread: ${Thread.currentThread().name}")
                    delay(1000)
                }
                println("withContextSwitch: Coroutine 1 is running on thread: ${Thread.currentThread().name}")
            }

            val endTime = System.currentTimeMillis()
            println("withContextSwitch: Time taken = ${endTime - startTime} ms")
        }
    }


    @Test
    fun orderTest() {
        runBlocking {
//            0: Start
//            4: WithContext start
//            5: WithContext end
//            1: Launch done
//            2: Deferred1 done
//            3: Deferred2 done
//            6: Results = [result1, result2]
//            7: WithContext = contextResult
//            8: End
            println("0: Start")

            val job = launch {
                delay(500)
                println("1: Launch done")
            }

            val deferred1 = async {
                delay(1000)
                println("2: Deferred1 done")
                "result1"
            }

            val deferred2 = async(start = CoroutineStart.LAZY) {
                delay(1000)
                println("3: Deferred2 done")
                "result2"
            }

            val ctxResult = withContext(Dispatchers.Default) {
                println("4: WithContext start")
                delay(300)
                println("5: WithContext end")
                "contextResult"
            }

            deferred2.start()
            job.join()
            val results = awaitAll(deferred1, deferred2)

            println("6: Results = $results")
            println("7: WithContext = $ctxResult")
            println("8: End")
        }

    }
}