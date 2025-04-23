package org.yunhalee.kotlin.coroutine

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.coroutines.CoroutineContext

class StructuredConcurrency {

    @Test
    fun `child coroutine implements parent coroutine context`() {
        // 부모 코루틴의 컨텍스트를 상속받는 자식 코루틴을 생성
        runBlocking {
            val coroutineContext = newSingleThreadContext("ThreadA") + CoroutineName("CoroutineA")
            val parentJob = launch(coroutineContext) {
                println("Parent coroutine: ${this.coroutineContext[CoroutineName.Key]}")
                println("parent thread name: ${Thread.currentThread().name}")
                val childJob = launch {
                    println("Child coroutine: ${this.coroutineContext[CoroutineName.Key]}")
                    println("child thread name: ${Thread.currentThread().name}")
                }
                childJob.join()
            }
            parentJob.join()
        }
    }

    @Test
    fun `child coroutine context overrides parent coroutine context`() {
        // 부모 코루틴의 컨텍스트를 상속받는 자식 코루틴을 생성
        runBlocking {
            val coroutineContext = newSingleThreadContext("ThreadA") + CoroutineName("CoroutineA")
            val parentJob = launch(context = coroutineContext) {
                println("Parent coroutine: ${this.coroutineContext[CoroutineName.Key]}")
                println("parent thread name: ${Thread.currentThread().name}")
                val childJob = launch(context = CoroutineName("ChildCoroutineA")) {
                    println("Child coroutine: ${this.coroutineContext[CoroutineName.Key]}")
                    println("child thread name: ${Thread.currentThread().name}")
                }
                childJob.join()
            }
            parentJob.join()
        }
    }

    @Test
    fun `child coroutine context doesn't override job from parent coroutine context`() {
        runBlocking {
            val coroutineContext = newSingleThreadContext("ThreadA") + CoroutineName("CoroutineA")
            val parentJob2 = launch(context = coroutineContext) {
                val parentJob = this.coroutineContext[Job]
                println("Parent coroutine: ${this.coroutineContext[CoroutineName.Key]}")
                println("parent thread name: ${Thread.currentThread().name}")
                val childJob = launch() {
                    val childJob = this.coroutineContext[Job]
                    println("Child coroutine: ${this.coroutineContext[CoroutineName.Key]}")
                    println("child thread name: ${Thread.currentThread().name}")
                    if (parentJob === childJob) {
                        println("parentJob === childJob")
                    } else {
                        println("parentJob !== childJob")
                    }
                }
                childJob.join()
            }
            parentJob2.join()
        }
    }


    @Test
    fun `child coroutine has parent property same as parent coroutine`() {
        runBlocking {
            val coroutineContext = newSingleThreadContext("ThreadA") + CoroutineName("CoroutineA")
            val parentJob2 = launch(context = coroutineContext) {
                val parentJob = this.coroutineContext[Job]
                val childJob = launch() {
                    val childJob = this.coroutineContext[Job]
                    println("부모 코루틴과 자식 코루틴의 Job이 같은가?" + (parentJob === childJob))
                    println("자식 코루틴의 Job이 가지는 parent property가 부모 코루틴과 같은가?" + (parentJob === childJob?.parent))
                    println("부모 코루틴의 Job이 가지는 child property가 자식 코루틴과 같은가?" + (parentJob?.children?.contains(childJob)))
                }
                childJob.join()
            }
            parentJob2.join()
        }

//        부모 코루틴과 자식 코루틴의 Job이 같은가?false
//        자식 코루틴의 Job이 가지는 parent property가 부모 코루틴과 같은가?true
//        부모 코루틴의 Job이 가지는 child property가 자식 코루틴과 같은가?true
    }

    @Test
    fun `parent job cancellation propagate to child job`() {
        runBlocking {
            val coroutineContext = newSingleThreadContext("ThreadA") + CoroutineName("CoroutineA")
            val parentJob = launch(context = coroutineContext) {
                val childDeferred: List<Deferred<String>> = listOf("1", "2", "3").map { number ->
                    async {
                        delay(1000)
                        println("Child coroutine: $number")
                        println("$number is done")
                        return@async number
                    }
                }
                childDeferred.awaitAll()
                println("child coroutine is done")
            }
            parentJob.cancel()
            // parentJob.cancel()가 호출되면 자식 코루틴도 취소된다.
            // 아무런 로그도 출력되지 않는다.
        }
    }

    @Test
    fun `parent coroutine can be finished when child coroutine is done`() {
        runBlocking {
            val startTime = System.currentTimeMillis()
            val coroutineContext = newSingleThreadContext("ThreadA") + CoroutineName("CoroutineA")
            val parentJob = launch(context = coroutineContext) {
                launch {
                    delay(1000)
                    println("Child coroutine is done")
                    val endTime = System.currentTimeMillis()
                    println("Child coroutine is done after ${endTime - startTime} ms")
                }
                println("parent coroutine last log")
            }
            parentJob.invokeOnCompletion {
                val endTime = System.currentTimeMillis()
                println("parent coroutine is done")
                println("parent coroutine is done after ${endTime - startTime} ms")
            }
        }

//        parent coroutine last log
//        Child coroutine is done
//        Child coroutine is done after 1018 ms
//        parent coroutine is done
//        parent coroutine is done after 1022 ms
    }


    @Test
    fun `parent coroutine is completing until child coroutine is done`() {
        runBlocking {
            val startTime = System.currentTimeMillis()
            val coroutineContext = newSingleThreadContext("ThreadA") + CoroutineName("CoroutineA")
            val parentJob = launch(context = coroutineContext) {
                launch {
                    delay(1000)
                    println("Child coroutine is done")
                    val endTime = System.currentTimeMillis()
                    println("Child coroutine is done after ${endTime - startTime} ms")
                }
                println("parent coroutine last log")
            }
            parentJob.invokeOnCompletion {
                val endTime = System.currentTimeMillis()
                println("parent coroutine is done")
                println("parent coroutine is done after ${endTime - startTime} ms")
            }
            delay(500)
            println("parentJob isActive : " + parentJob.isActive)
            println("parentJob isCompleted : " + parentJob.isCompleted)
            println("parentJob isCancelled : " + parentJob.isCancelled)
        }
    }


    @Test
    fun `Custom Coroutine Scope`() {
        runBlocking {
            val coroutineScope = CustomCoroutineScope()
            coroutineScope.launch {
                delay(1000)
                println("threadName: ${Thread.currentThread().name} 코루틴 실행 완료")
            }
            Thread.sleep(1000)
        }
    }


    class CustomCoroutineScope : CoroutineScope {
        override val coroutineContext: CoroutineContext = newSingleThreadContext("CustomCoroutineScope") + CoroutineName("CustomCoroutineScope")
    }


    @Test
    fun `Coroutine Scope`() {
        runBlocking {
            CoroutineScope(Dispatchers.IO).launch {
                delay(1000)
                println("threadName: ${Thread.currentThread().name} 코루틴 실행 완료")
            }
            Thread.sleep(1000)
        }
    }


    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun `Coroutine Scope launch with job `() {
        runBlocking {
            val parentContext = CoroutineScope(CoroutineName("CoroutineParent") + Dispatchers.IO)
            parentContext.launch(CoroutineName("CoroutineChild")) {
                println("threadName: ${Thread.currentThread().name} 코루틴 실행 완료")
                println("CoroutineName: ${this.coroutineContext[CoroutineName.Key]}")
                println("dispatcher: ${this.coroutineContext[CoroutineDispatcher]}")
                val childScopeJob = this.coroutineContext[Job]
                val parentScopeJob = parentContext.coroutineContext[Job]
                println("parentScopeJob === childScopeJob.parent : ${parentScopeJob === childScopeJob?.parent}")
            }
            Thread.sleep(1000)
        }
    }

    @Test
    fun `Coroutine scope range`() {
        runBlocking { // runBlocking은 코루틴 스코프 시작
            launch(CoroutineName("Coroutine1")) {  // Coroutine1 코루틴 스코프 시작
                launch(CoroutineName("Coroutine2")) { // Coroutine2 코루틴 스코프 시작
                    println("ThreadName: ${Thread.currentThread().name} 코루틴 실행 완료")
                } // Coroutine2 코루틴 스코프 종료
                launch(CoroutineName("Coroutine3")) { // Coroutine3 코루틴 스코프 시작
                    println("ThreadName: ${Thread.currentThread().name} 코루틴 실행 완료")
                } // Coroutine3 코루틴 스코프 종료
            } // Coroutine1 코루틴 스코프 종료

            launch(CoroutineName("Coroutine4")) { // Coroutine4 코루틴 스코프 시작
                println("ThreadName: ${Thread.currentThread().name} 코루틴 실행 완료")
            } // Coroutine4 코루틴 스코프 종료

        }// runBlocking 코루틴 스코프 종료
    }


    @Test
    fun `Coroutine scope range with different scope`() {
        runBlocking { // runBlocking은 코루틴 스코프 시작
            launch(CoroutineName("Coroutine1")) {  // Coroutine1 코루틴 스코프 시작
                launch(CoroutineName("Coroutine2")) { // Coroutine2 코루틴 스코프 시작
                    println("ThreadName: ${Thread.currentThread().name} 코루틴 실행 완료")
                } // Coroutine2 코루틴 스코프 종료
                CoroutineScope(Dispatchers.IO).launch(CoroutineName("Coroutine3")) { // Coroutine3 코루틴 스코프 시작 (runBlocking과 다른 스코프))
                    println("ThreadName: ${Thread.currentThread().name} 코루틴 실행 완료")
                } // Coroutine3 코루틴 스코프 종료
            } // Coroutine1 코루틴 스코프 종료

            launch(CoroutineName("Coroutine4")) { // Coroutine4 코루틴 스코프 시작
                println("ThreadName: ${Thread.currentThread().name} 코루틴 실행 완료")
            } // Coroutine4 코루틴 스코프 종료

        }// runBlocking 코루틴 스코프 종료
    }


    @Test
    fun `Coroutine Scope cancel`() {
        runBlocking { // runBlocking은 코루틴 스코프 시작
            launch(CoroutineName("Coroutine1")) {  // Coroutine1 코루틴 스코프 시작
                launch(CoroutineName("Coroutine2")) { // Coroutine2 코루틴 스코프 시작
                    delay(100)
                    println("ThreadName: ${Thread.currentThread().name} 코루틴 실행 완료")
                } // Coroutine2 코루틴 스코프 종료
                launch(CoroutineName("Coroutine3")) { // Coroutine3 코루틴 스코프 시작 (runBlocking과 다른 스코프))
                    delay(100)
                    println("ThreadName: ${Thread.currentThread().name} 코루틴 실행 완료")
                } // Coroutine3 코루틴 스코프 종료
                this.cancel()
            } // Coroutine1 코루틴 스코프 종료

            launch(CoroutineName("Coroutine4")) { // Coroutine4 코루틴 스코프 시작
                delay(100)
                println("ThreadName: ${Thread.currentThread().name} 코루틴 실행 완료")
            } // Coroutine4 코루틴 스코프 종료

        }// runBlocking 코루틴 스코프 종료

        // ThreadName: Test worker @Coroutine4#3 코루틴 실행 완료
    }

    @Test
    fun `Coroutine Scope isActive`() {
        runBlocking {
            val job = launch(Dispatchers.Default) {
                while (this.isActive) {
                    println("Coroutine is working")
                }
            }
            delay(100)
            job.cancel()
        }
    }

    @Test
    fun `main thread runBlocking with new coroutine scope`() {
        runBlocking {
            CoroutineScope(Dispatchers.IO).launch() {
                delay(1000)
                println("new coroutine scope is done")
            }
            println("main thread is done")
        }
        // main thread is done
    }


    @Test
    fun `main thread runBlocking with new coroutine scope - waiting`() {
        runBlocking {
            CoroutineScope(Dispatchers.IO).launch() {
                delay(1000)
                println("new coroutine scope is done")
            }
            delay(1200)
            println("main thread is done")
        }
        // new coroutine scope is done
        // main thread is done
    }

    @Test
    fun `create new root Job with Job()`() {
        runBlocking {
            val job = Job()
            launch(context = CoroutineName("CoroutineA") + job) {
                launch(CoroutineName("CoroutineB")) {
                    delay(100)
                    println(" thread name: ${Thread.currentThread().name}")
                }
                launch(CoroutineName("CoroutineC")) {
                    delay(100)
                    println(" thread name: ${Thread.currentThread().name}")
                }
            }
            launch(CoroutineName("CoroutineD") + job) {
                launch(CoroutineName("CoroutineE")) {
                    delay(100)
                    println(" thread name: ${Thread.currentThread().name}")
                }
            }
            delay(1000)
        }
    }

    @Test
    fun `create new root Job with Job() extending exception`() {
        // 취소가 전파되어 아무런 로그가 찍히지 않게 됨
        runBlocking {
            val job = Job()
            launch(context = CoroutineName("CoroutineA") + job) {
                launch(CoroutineName("CoroutineB")) {
                    delay(100)
                    println(" thread name: ${Thread.currentThread().name}")
                }
                launch(CoroutineName("CoroutineC")) {
                    delay(100)
                    println(" thread name: ${Thread.currentThread().name}")
                }
            }
            launch(CoroutineName("CoroutineD") + job) {
                launch(CoroutineName("CoroutineE")) {
                    delay(100)
                    println(" thread name: ${Thread.currentThread().name}")
                }
            }
            job.cancel()
            delay(1000)
        }
    }


    @Test
    fun `create new root Job with Job() extending exception except another job`() {
        // 취소가 전파되어 아무런 로그가 찍히지 않게 됨
        runBlocking {
            val job = Job()
            val newJob = Job()
            launch(context = CoroutineName("CoroutineA") + job) {
                launch(CoroutineName("CoroutineB")) {
                    delay(100)
                    println(" thread name: ${Thread.currentThread().name}")
                }
                launch(CoroutineName("CoroutineC")) {
                    delay(100)
                    println(" thread name: ${Thread.currentThread().name}")
                }
            }
            launch(CoroutineName("CoroutineD") + job) {
                launch(CoroutineName("CoroutineE") + newJob) {
                    delay(100)
                    println(" thread name: ${Thread.currentThread().name}")
                }
            }
            delay(50)
            job.cancel()
            delay(1000)
        }
    }

    @Test
    fun `create Job() without breaking structure`() {
        runBlocking {
            launch(CoroutineName("CoroutineA")) {
                val job = Job(parent = this.coroutineContext[Job])
                launch(CoroutineName("CoroutineB") + job) {
                    delay(100)
                    println(" thread name: ${Thread.currentThread().name} 코루틴 실행")
                }
            }
        }
    }

    @Test
    fun `creating job with Job() should explicitly complete`() {
        runBlocking {
            launch(context = CoroutineName("CoroutineA")) {
                val job = Job()
                launch(CoroutineName("CoroutineB")) {
                    delay(100)
                    println(" thread name: ${Thread.currentThread().name} 코루틴 실행")
                }
                job.complete()
            }
        }
    }

    @Test
    fun `runBlocking blocks calling thread`() {
        runBlocking {
            println("runBlocking start")
            delay(1000)
            println("runBlocking end")
        }
        println("runBlocking is done")
    }

    @Test
    fun `runBlocking blocks calling thread - without using`() {
        runBlocking(Dispatchers.IO) {
            println("runBlocking start")
            delay(1000)
            println("runBlocking end")
        }
        println("runBlocking is done")
    }

    @Test
    fun `runBlocking exclusive blocks calling thread`() {
        runBlocking {
            launch {
                println("launch start")
                println("threadName: ${Thread.currentThread().name}")
                delay(1000)
                println("launch end")
            }
            println("runBlocking start")
            println("threadName: ${Thread.currentThread().name}")
            delay(1000)
            println("runBlocking end")
        }
        println("runBlocking is done")
    }

    @Test
    fun `runBlocking with launch`() {
        runBlocking {
            launch {
                println("launch start")
                println("threadName: ${Thread.currentThread().name}")
                delay(1000)
                println("launch end")
            }
            println("runBlocking start")
            println("threadName: ${Thread.currentThread().name}")
            println("runBlocking end")
        }
        println("runBlocking is done")
//        runBlocking start
//        threadName: Test worker @coroutine#1
//        runBlocking end
//        launch start
//        threadName: Test worker @coroutine#2
//        launch end
//        runBlocking is done
    }
}