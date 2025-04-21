package org.yunhalee.kotlin.coroutine

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class CoroutineContextTest {

    @Test
    fun `coroutine Context + coroutine Context`() {
        val coroutineContext = newSingleThreadContext("coroutineContext") + CoroutineName("coroutineContext") + newSingleThreadContext("coroutineContext2")
        runBlocking {
            launch(context = coroutineContext) {
                println("threadName: ${Thread.currentThread().name}")
            }
        }
        println(coroutineContext[CoroutineName])
//        threadName: coroutineContext2 @coroutineContext#2
//        CoroutineName(coroutineContext)
    }

    @Test
    fun `create job with job()`() {
        val job = Job()
        val coroutineContext = Dispatchers.IO + job
        val coroutineScope = CoroutineScope(coroutineContext)
        coroutineScope.launch {
            println("threadName: ${Thread.currentThread().name}")
        }
        assertTrue { job.isActive }
//        threadName: DefaultDispatcher-worker-1 @coroutine#1
    }

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun `CoroutineDispatcher Key`() {
        val coroutineContext = newSingleThreadContext("coroutineContext") + CoroutineName("coroutineContext") + newSingleThreadContext("coroutineContext2")
        runBlocking {
            launch(context = coroutineContext) {
                println("threadName: ${Thread.currentThread().name}")
            }
        }
        println(coroutineContext[CoroutineDispatcher.Key])
    }

    @Test
    fun `CoroutineDispatcher key`() {
        val dispatcherKey1 = Dispatchers.IO.key
        val dispatcherKey2 = Dispatchers.Default.key
        assertTrue { dispatcherKey1 === dispatcherKey2 }
    }

    @Test
    fun `CoroutineContext get property by key`() {
        val coroutineContext = newSingleThreadContext("coroutineContext") + CoroutineName("coroutineContext") + newSingleThreadContext("coroutineContext2")
        runBlocking {
            launch(context = coroutineContext) {
                println("threadName: ${Thread.currentThread().name}")
            }
        }
        println(coroutineContext[CoroutineName.Key])
        // CoroutineName(coroutineContext)
    }

    @Test
    fun `CoroutineContext get property by property`() {
        val coroutineName = CoroutineName("coroutineContext")
        val dispatcher = Dispatchers.IO
        val coroutineContext = coroutineName + dispatcher
        runBlocking {
            launch(context = coroutineContext) {
                println("threadName: ${Thread.currentThread().name}")
            }
        }
        println(coroutineContext[coroutineName.key])
        println(coroutineContext[dispatcher.key])
//        CoroutineName(coroutineContext)
//        Dispatchers.IO
    }

    @Test
    fun `minusKey`() {
        val coroutineName = CoroutineName("coroutineContext")
        val dispatcher = Dispatchers.IO
        val coroutineContext = coroutineName + dispatcher
        val coroutineContext2 = coroutineContext.minusKey(coroutineName.key)
        runBlocking {
            launch(context = coroutineContext) {
                println("1: threadName: ${Thread.currentThread().name}")
            }

            launch(context = coroutineContext2) {
                println("2: threadName: ${Thread.currentThread().name}")
            }
        }
        println("coroutineContextName: ${coroutineContext[coroutineName.key]}")
        println("coroutineContext2Name: ${coroutineContext2[coroutineName.key]}")
//        1: threadName: DefaultDispatcher-worker-1 @coroutineContext#2
//        2: threadName: DefaultDispatcher-worker-3 @coroutine#3
//        coroutineContextName: CoroutineName(coroutineContext)
//        coroutineContext2Name: null
    }
}