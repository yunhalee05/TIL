package org.yunhalee.kotlin.coroutine

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.jupiter.api.Test

class CoroutineExceptionHandlerTest {

    @Test
    fun `child coroutine exception propagate to parent coroutine and cancel all child jobs`() {
        runBlocking {
            launch {
                try {
                    repeat(5) { i ->
                        delay(200)
                        println("A: Working $i")
                    }
                } finally {
                    println("A: Cancelled due to sibling failure")
                }
            }

            launch {
                launch {
                    delay(500)
                    println("B: About to throw exception")
                    throw RuntimeException("B: Something went wrong!")
                }

                launch {
                    println("C: Working")
                }
            }

            launch {
                try {
                    repeat(5) { i ->
                        delay(300)
                        println("D: Working $i")
                    }
                } finally {
                    println("D: Cancelled due to sibling failure")
                }
            }
        }
        println("Main: Done")

//        C: Working
//        A: Working 0
//        D: Working 0
//        A: Working 1
//        B: About to throw exception
//        A: Cancelled due to sibling failure
//        D: Cancelled due to sibling failure
    }

    @Test
    fun `disrupt exception propagation - using Job()`() {
        runBlocking {
            launch {
                repeat(5) { i ->
                    delay(200)
                    println("A: Working $i")
                }
            }

            launch(Job()) {
                launch {
                    println("B: About to throw exception")
                    throw RuntimeException("B: Something went wrong!")
                }

                launch {
                    delay(100)
                    println("C: Working")
                }
            }
            launch {
                repeat(5) { i ->
                    delay(300)
                    println("D: Working $i")
                }
            }
        }
        println("Main: Done")

//        C: Working
//        A: Working 0
//        A: Working 1
//        B: About to throw exception
//        Exception in thread "Test worker @coroutine#4" java.lang.RuntimeException: B: Something went wrong!
//        A: Working 2
//        A: Working 3
//        A: Working 4
//        Main: Done
    }


    @Test
    fun `disrupt exception propagation - using SupervisorJob()`() {
        runBlocking {
            val supervisorJob = SupervisorJob()
            launch {
                repeat(5) { i ->
                    delay(200)
                    println("A: Working $i + Thread: ${Thread.currentThread().name}")
                }
            }

            launch(supervisorJob) {
                launch {
                    println("B: About to throw exception + Thread: ${Thread.currentThread().name}")
                    throw RuntimeException("B: Something went wrong!")
                }

                launch {
                    delay(100)
                    println("C: Working + Thread: ${Thread.currentThread().name}")
                }
            }
            launch(supervisorJob) { // 같은 supervisorJob을 사용 -> 예외가 전파되지 않음
                repeat(5) { i ->
                    delay(300)
                    println("D: Working $i + Thread: ${Thread.currentThread().name}")
                }
            }
        }
        println("Main: Done")
    }

    @Test
    fun `disrupt exception propagation - using SupervisorJob()(without breaking structure)`() {
        runBlocking {
            val supervisorJob = SupervisorJob(parent = this.coroutineContext[Job])
            launch {
                repeat(5) { i ->
                    delay(200)
                    println("A: Working $i + Thread: ${Thread.currentThread().name}")
                }
            }

            launch(supervisorJob) {
                launch {
                    println("B: About to throw exception + Thread: ${Thread.currentThread().name}")
                    throw RuntimeException("B: Something went wrong!")
                }

                launch {
                    delay(100)
                    println("C: Working + Thread: ${Thread.currentThread().name}")
                }
            }
            launch(supervisorJob) { // 같은 supervisorJob을 사용 -> 예외가 전파되지 않음
                repeat(5) { i ->
                    delay(300)
                    println("D: Working $i + Thread: ${Thread.currentThread().name}")
                }
            }
            supervisorJob.complete()
        }

        println("Main: Done")

//        B: About to throw exception + Thread: Test worker @coroutine#5
//        Exception in thread "Test worker @coroutine#6" java.lang.RuntimeException: B: Something went wrong!
//        A: Working 0 + Thread: Test worker @coroutine#2
//        D: Working 0 + Thread: Test worker @coroutine#4
//        A: Working 1 + Thread: Test worker @coroutine#2
//        D: Working 1 + Thread: Test worker @coroutine#4
//        A: Working 2 + Thread: Test worker @coroutine#2
//        A: Working 3 + Thread: Test worker @coroutine#2
//        D: Working 2 + Thread: Test worker @coroutine#4
//        A: Working 4 + Thread: Test worker @coroutine#2
//        D: Working 3 + Thread: Test worker @coroutine#4
//        D: Working 4 + Thread: Test worker @coroutine#4
//        Main: Done
    }

    @Test
    fun `disrupt exception propagation - using SupervisorJob()(with CoroutineScope)`() {
        runBlocking {
            val coroutineScope = CoroutineScope(SupervisorJob())
            coroutineScope.apply {
                launch {
                    repeat(5) { i ->
                        delay(200)
                        println("A: Working $i + Thread: ${Thread.currentThread().name}")
                    }
                }

                launch {
                    launch {
                        println("B: About to throw exception + Thread: ${Thread.currentThread().name}")
                        throw RuntimeException("B: Something went wrong!")
                    }

                    launch {
                        delay(100)
                        println("C: Working + Thread: ${Thread.currentThread().name}")
                    }
                }
                launch { // 같은 supervisorJob을 사용 -> 예외가 전파되지 않음
                    repeat(5) { i ->
                        delay(300)
                        println("D: Working $i + Thread: ${Thread.currentThread().name}")
                    }
                }
                delay(2000)
            }
        }
        println("Main: Done")

//        B: About to throw exception + Thread: DefaultDispatcher-worker-4 @coroutine#5
//        Exception in thread "DefaultDispatcher-worker-5 @coroutine#6" java.lang.RuntimeException: B: Something went wrong!
//        A: Working 0 + Thread: DefaultDispatcher-worker-5 @coroutine#2
//        D: Working 0 + Thread: DefaultDispatcher-worker-5 @coroutine#4
//        A: Working 1 + Thread: DefaultDispatcher-worker-5 @coroutine#2
//        D: Working 1 + Thread: DefaultDispatcher-worker-5 @coroutine#4
//        A: Working 2 + Thread: DefaultDispatcher-worker-5 @coroutine#2
//        A: Working 3 + Thread: DefaultDispatcher-worker-5 @coroutine#2
//        D: Working 2 + Thread: DefaultDispatcher-worker-5 @coroutine#4
//        A: Working 4 + Thread: DefaultDispatcher-worker-5 @coroutine#2
//        D: Working 3 + Thread: DefaultDispatcher-worker-5 @coroutine#4
//        D: Working 4 + Thread: DefaultDispatcher-worker-5 @coroutine#4
//        Main: Done
    }


    @Test
    fun `disrupt exception propagation - using supervisorScope`() {
        runBlocking {
            supervisorScope {
                launch {
                    repeat(5) { i ->
                        delay(200)
                        println("A: Working $i + Thread: ${Thread.currentThread().name}")
                    }
                }

                launch {
                    launch {
                        println("B: About to throw exception + Thread: ${Thread.currentThread().name}")
                        throw RuntimeException("B: Something went wrong!")
                    }

                    launch {
                        delay(100)
                        println("C: Working + Thread: ${Thread.currentThread().name}")
                    }
                }
                launch { // 같은 supervisorJob을 사용 -> 예외가 전파되지 않음
                    repeat(5) { i ->
                        delay(300)
                        println("D: Working $i + Thread: ${Thread.currentThread().name}")
                    }
                }
                delay(2000)
            }
        }
        println("Main: Done")
    }


    @Test
    fun `coroutineExceptionHandler`() {
        runBlocking {
            val exceptionHandler = CoroutineExceptionHandler { _, exception ->
                println("Caught $exception")
            }

            CoroutineScope(exceptionHandler).launch(CoroutineName("Coroutine1")) {
                println("Throwing exception from coroutine")
                println("Thread name: ${Thread.currentThread().name}")
                throw RuntimeException("Coroutine Exception")
            }
        }
        println("Main: Done")


//        Main: Done
//        Throwing exception from coroutine
//        Thread name: DefaultDispatcher-worker-1 @Coroutine1#2
//        Caught java.lang.RuntimeException: Coroutine Exception
    }

    @Test
    fun `coroutineExceptionHandler - parent exception handling`() {
        runBlocking {
            val exceptionHandler = CoroutineExceptionHandler { _, exception ->
                println("Caught $exception")
            }

            launch(CoroutineName("Coroutine1") + exceptionHandler) {
                println("Throwing exception from coroutine")
                println("Thread name: ${Thread.currentThread().name}")
                throw RuntimeException("Coroutine Exception")
            }
        }
        println("Main: Done")

//        Throwing exception from coroutine
//        Thread name: Test worker @Coroutine1#2
//
//        Coroutine Exception
//            java.lang.RuntimeException: Coroutine Exception
    }


    @Test
    fun `coroutineExceptionHandler - correct structure`() {
        runBlocking {
            val exceptionHandler = CoroutineExceptionHandler { _, exception ->
                println("Caught $exception")
            }

            CoroutineScope(exceptionHandler).launch(CoroutineName("Coroutine1")) {
                println("Throwing exception from coroutine")
                println("Thread name: ${Thread.currentThread().name}")
                throw RuntimeException("Coroutine Exception")
            }
        }
        println("Main: Done")
    }


    @Test
    fun `coroutineExceptionHandler - with Job()`() {
        runBlocking {
            val exceptionHandler = CoroutineExceptionHandler { _, exception ->
                println("Caught $exception")
            }

            launch(CoroutineName("Coroutine1") + Job() + exceptionHandler) {
                println("Throwing exception from coroutine")
                println("Thread name: ${Thread.currentThread().name}")
                throw RuntimeException("Coroutine Exception")
            }
        }
        println("Main: Done")

//        Throwing exception from coroutine
//        Thread name: Test worker @Coroutine1#2
//        Caught java.lang.RuntimeException: Coroutine Exception
//        Main: Done
    }


    @Test
    fun `coroutineExceptionHandler - with SupervisorJob()`() {
        runBlocking {
            val exceptionHandler = CoroutineExceptionHandler { _, exception ->
                println("Caught $exception")
            }

            launch(CoroutineName("Coroutine1") + SupervisorJob() + exceptionHandler) {
                println("Throwing exception from coroutine")
                println("Thread name: ${Thread.currentThread().name}")
                throw RuntimeException("Coroutine Exception")
            }
        }
        println("Main: Done")

//        Throwing exception from coroutine
//        Thread name: Test worker @Coroutine1#2
//        Caught java.lang.RuntimeException: Coroutine Exception
//        Main: Done
    }


    @Test
    fun `coroutineExceptionHandler - with supervisorScope`() {
        runBlocking {
            val exceptionHandler = CoroutineExceptionHandler { _, exception ->
                println("Caught $exception")
            }

            val supervisorScope = CoroutineScope(SupervisorJob() + exceptionHandler)

            supervisorScope.apply {
                launch(CoroutineName("Coroutine1")) {
                    println("Throwing exception from coroutine")
                    println("Thread name: ${Thread.currentThread().name}")
                    throw RuntimeException("Coroutine Exception")
                }
            }
        }
        println("Main: Done")
//        Throwing exception from coroutine
//        Main: Done
//        Thread name: DefaultDispatcher-worker-1 @Coroutine1#2
//        Caught java.lang.RuntimeException: Coroutine Exception
    }


    @Test
    fun `coroutineExceptionHandler - propagate exception`() {
        runBlocking {
            val exceptionHandler = CoroutineExceptionHandler { _, exception ->
                println("Caught $exception")
            }
            launch(CoroutineName("Coroutine1") + exceptionHandler) {
                println("Throwing exception from coroutine")
                println("Thread name: ${Thread.currentThread().name}")
                throw RuntimeException("Coroutine Exception")

            }
        }
        println("Main: Done")

//        Coroutine Exception
//            java.lang.RuntimeException: Coroutine Exception
    }


    @Test
    fun `coroutineExceptionHandle with try - catch`() {
        runBlocking {
            launch(CoroutineName("Coroutine1")) {
                try {
                    println("Throwing exception from coroutine")
                    println("Thread name: ${Thread.currentThread().name}")
                    throw RuntimeException("Coroutine Exception")
                } catch (e: Exception) {
                    println("Caught $e")
                }
            }

            launch(CoroutineName("Coroutine2")) {
                println("Coroutine2 is running")
                delay(1000)
            }
        }
//        println("Main: Done")
//        Throwing exception from coroutine
//        Thread name: Test worker @Coroutine1#2
//        Caught java.lang.RuntimeException: Coroutine Exception
//        Coroutine2 is running
//        Main: Done
    }


    @Test
    fun `coroutineExceptionHandle with try - catch to coroutine builder not working`() {
        runBlocking {
            try {
                launch(CoroutineName("Coroutine1")) {
                    println("Throwing exception from coroutine")
                    println("Thread name: ${Thread.currentThread().name}")
                    throw RuntimeException("Coroutine Exception")
                }
            } catch (e: Exception) {
                println("Caught $e")
            }

            launch(CoroutineName("Coroutine2")) {
                println("Coroutine2 is running")
                delay(1000)
            }
        }
        println("Main: Done")
//        Throwing exception from coroutine
//            Thread name: Test worker @Coroutine1#2
//
//        Coroutine Exception
//            java.lang.RuntimeException: Coroutine Exception
    }

    @Test
    fun `async-await exception handling`() {
        runBlocking {
            supervisorScope {
                val deferred = async {
                    println("Throwing exception from async")
                    println("Thread name: ${Thread.currentThread().name}")
                    throw RuntimeException("Async Exception")
                }

                try {
                    deferred.await()
                } catch (e: Exception) {
                    println("Caught $e")
                }
            }
        }
        println("Main: Done")
//        Throwing exception from async
//        Thread name: Test worker @coroutine#2
//        Caught java.lang.RuntimeException: Async Exception
//        Main: Done
    }


    @Test
    fun `async exception handling`() {
        runBlocking {
            async(CoroutineName("Coroutine1")) {
                println("Throwing exception from async")
                println("Thread name: ${Thread.currentThread().name}")
                throw RuntimeException("Async Exception")
            }
            launch(CoroutineName("Coroutine2")) {
                delay(100)
                println("Coroutine2 is running")
            }
        }
        println("Main: Done")
//        Throwing exception from async
//            Thread name: Test worker @Coroutine1#2
//
//        Async Exception
//            java.lang.RuntimeException: Async Exception
    }


    @Test
    fun `async exception handling - with supervisorScope`() {
        runBlocking {
            supervisorScope {
                async(CoroutineName("Coroutine1")) {
                    println("Throwing exception from async")
                    println("Thread name: ${Thread.currentThread().name}")
                    throw RuntimeException("Async Exception")
                }
                launch(CoroutineName("Coroutine2")) {
                    delay(100)
                    println("Coroutine2 is running")
                }
            }
        }
        println("Main: Done")
//        Throwing exception from async
//        Thread name: Test worker @Coroutine1#2
//        Coroutine2 is running
//        Main: Done
    }


    @Test
    fun `cancellationException does not propgate`() {
        runBlocking {

            launch(CoroutineName("Coroutine1")) {
                println("Throwing CancellationException from coroutine")
                println("Thread name: ${Thread.currentThread().name}")
                throw CancellationException("Coroutine Exception")
            }

            launch(CoroutineName("Coroutine2")) {
                println("Coroutine2 is running")
                delay(1000)
            }
        }
        println("Main: Done")
//        Throwing CancellationException from coroutine
//        Thread name: Test worker @Coroutine1#2
//        Coroutine2 is running
//        Main: Done
    }

    @Test
    fun `cancellationException with invokeOnCompletion`() {
        runBlocking {
            val job = launch(CoroutineName("Coroutine1")) {
                delay(1000)
            }

            job.invokeOnCompletion { exception ->
                println(exception)
            }
            job.cancel()
        }
//        kotlinx.coroutines.JobCancellationException: StandaloneCoroutine was cancelled; job="Coroutine1#2":StandaloneCoroutine{Cancelled}@5ba88be8
    }

    @Test
    fun `withTimeout - TimeoutCancellationException`() {
        runBlocking(CoroutineName("Parent Coroutine")) {
            launch(CoroutineName("Child Coroutine")) {
                withTimeout(1000) {
                    delay(2000)
                    println("Thread name: ${Thread.currentThread().name}")
                }
            }
            delay(2000)
            println("Thread name: ${Thread.currentThread().name}")
        }
//        Thread name: Test worker @Parent Coroutine#1 // 자식은 실행되지 않음 및 예외 전파되지 않음
    }

    @Test
    fun `withTimeout - TimeoutCancellationException handling with try catch`() {
        runBlocking(CoroutineName("Parent Coroutine")) {
            launch(CoroutineName("Child Coroutine")) {
                try {
                    withTimeout(1000) {
                        delay(2000)
                        println("Thread name: ${Thread.currentThread().name}")
                    }
                } catch (e: TimeoutCancellationException) {
                    println("Caught $e")
                }
            }
            delay(2000)
            println("Thread name: ${Thread.currentThread().name}")
//            Caught kotlinx.coroutines.TimeoutCancellationException: Timed out waiting for 1000 ms
//            Thread name: Test worker @Parent Coroutine#1
        }
    }


    @Test
    fun `withTimeoutOrNull`() {
        runBlocking(CoroutineName("Parent Coroutine")) {
            launch(CoroutineName("Child Coroutine")) {
                val result = withTimeoutOrNull(1000) {
                    delay(2000)
                    println("Thread name: ${Thread.currentThread().name}")
                }
                println(result)
            }

            delay(2000)
            println("Thread name: ${Thread.currentThread().name}")
//            null
//            Thread name: Test worker @Parent Coroutine#1
        }
    }

    @Test
    fun `example test`() {
        runBlocking {
            val exceptionHandler = CoroutineExceptionHandler { _, exception ->
                println("Caught by handler: ${exception.message}")
            }

            val supervisor = SupervisorJob()
            val scope = CoroutineScope(coroutineContext + supervisor + exceptionHandler)

            val job1 = scope.launch {
                delay(500)
                println("Job1: Throwing exception!")
                throw RuntimeException("Job1 failed")
            }

            val job2 = scope.launch {
                repeat(5) { i ->
                    delay(200)
                    println("Job2: Working $i")
                }
            }

            job1.join()
            job2.join()

            println("Main: Done")
        }
    }

    @Test
    fun `example2 test`() {
        runBlocking {
            val handler = CoroutineExceptionHandler { ctx, e ->
                println("Caught by handler: ${e.message}")
            }

            val supervisor = SupervisorJob()
            val scope = CoroutineScope(coroutineContext + supervisor + handler)

            val deferred1 = scope.async {
                try {
                    repeat(3) { i ->
                        delay(200)
                        println("Deferred1: Working $i")
                    }
                    "Success from deferred1"
                } finally {
                    println("Deferred1: Finally executed")
                }
            }

            val deferred2 = scope.async {
                delay(300)
                println("Deferred2: Throwing exception")
                throw RuntimeException("Deferred2 failure")
            }

            val job = scope.launch {
                try {
                    repeat(5) { i ->
                        delay(150)
                        println("Job: Working $i")
                    }
                } finally {
                    println("Job: Finally executed")
                }
            }

            delay(700)
            println("Main: Awaiting deferred1 and deferred2")

            try {
                println("Result1: ${deferred1.await()}")
            } catch (e: Exception) {
                println("Deferred1 await failed: ${e.message}")
            }

            try {
                println("Result2: ${deferred2.await()}")
            } catch (e: Exception) {
                println("Deferred2 await failed: ${e.message}")
            }

            println("Main: End")
        }
    }

}