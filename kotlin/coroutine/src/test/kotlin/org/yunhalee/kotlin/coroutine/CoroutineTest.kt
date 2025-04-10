package org.yunhalee.kotlin.coroutine

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.Test
import kotlin.system.measureTimeMillis

class CoroutineTest {


    @Test
    fun `runBlocking should block the thread and delay other coroutines`() {
//        [[runBlocking] Started on TestSingleThread @coroutine#1, runBlocking blocking start on TestSingleThread @coroutine#1, runBlocking blocking end, runBlocking blocking duration: 1000 ms, launch start on TestSingleThread @coroutine#2, launch finished, launch duration: 107 ms]
//        Total time: 1142 ms
        // given
        // 단일 스레드 디스패처 생성
        val singleThreadDispatcher = newSingleThreadContext("TestSingleThread")

        // when
        // runBlocking이 단일 스레드에서 점유하면, 그 외 launch는 대기 상태가 됨
        singleThreadDispatcher.use { dispatcher ->
            val events = mutableListOf<String>()

            val time = measureTimeMillis {
                runBlocking(dispatcher) {
                    events.add("[runBlocking] Started on ${Thread.currentThread().name}")
                    // 코루틴의 즉시 실행 (Scheduling semantics)
                    launch { // 비동기적으로 예약됨 (scheduled) -> 코루틴 생성 + 디스패처에게 실행 요청 (scheduling) 되므로 바로 실행이 아닌 다음 이벤트 루프에 실행됨 = 예약만 하고 바로 다음 줄로 넘어감
                        val launchStartTime = System.currentTimeMillis()
                        events.add("launch start on ${Thread.currentThread().name}")
                        delay(100)  // 논블로킹이지만 runBlocking 점유 중이라 대기
                        val launchEndTime = System.currentTimeMillis()
                        events.add("launch finished")
                        events.add("launch duration: ${launchEndTime - launchStartTime} ms")
                    }
                    val runBlockingStartTime = System.currentTimeMillis()
                    events.add("runBlocking blocking start on ${Thread.currentThread().name}") // 현재 스레드에서 즉시 실행됨
                    Thread.sleep(1000)
                    val runBlockingEndTime = System.currentTimeMillis()
                    events.add("runBlocking blocking end")
                    events.add("runBlocking blocking duration: ${runBlockingEndTime - runBlockingStartTime} ms")
                }
            }
            println(events)
            println("Total time: $time ms")

            // then
            // 검증: launch는 runBlocking 블로킹 이후에 실행됨
            val runBlockingEndIndex = events.indexOf("runBlocking blocking end")
            val launchFinishedIndex = events.indexOf("launch finished")

            assert(runBlockingEndIndex < launchFinishedIndex) {
                "launch가 runBlocking의 블로킹 끝나기 전에 실행되었습니다"
            }

        }
    }

    @Test
    fun `runBlockingWithOthersTest`() {
//        1. runBlocking start - Test worker @coroutine#1
//        2. launch block (UNDISPATCHED) - Test worker @coroutine#3
//        2. launch block - Test worker @coroutine#2
//        4. withContext block - DefaultDispatcher-worker-1 @coroutine#1
//        3. async block - Test worker @coroutine#4
//        5. runBlocking block - Test worker @coroutine#5
//        6. runBlocking end - Test worker @coroutine#1
        runBlocking {
            println("1. runBlocking start - ${Thread.currentThread().name}") // 현재 테스트 실행 스레드에서 즉시 실행 - 현재 스레드를 블로킹하며 점유

            launch {
                println("2. launch block - ${Thread.currentThread().name}") // 디스패처에 예약되어 다음 이벤트 루프에서 실행됨 - 스케쥴링에 따라 실행 순서가 달라질 수 있음
            }

            launch(start = CoroutineStart.UNDISPATCHED) {
                println("2. launch block (UNDISPATCHED) - ${Thread.currentThread().name}") // 코루틴을 즉시 현재 스레드에서 실행 - runBlocking과 같은 스레드에서 바로 실행됨
            }

            async {
                println("3. async block - ${Thread.currentThread().name}") // 예약 방식 - runBlocking의 메인 스레드에 다시 스케줄됨
            }

            withContext(Dispatchers.Default) {
                println("4. withContext block - ${Thread.currentThread().name}") // 새로운 디스패처로 스레드를 전환하며 실행 - Default 디스패처 내부적으로 ForkJoinPool 기반의 background 스레드, 즉시 실행되며 완료될 때까지 runBlocking은 기다림
            }

            runBlocking {
                println("5. runBlocking block - ${Thread.currentThread().name}") // 내부 runBlocking 즉시 동기 실행 - 실행되는 시점에 혀냊 스레드 점유
            }

            println("6. runBlocking end - ${Thread.currentThread().name}") // 원래 스레드로 복귀됨
        }
    }

    @Test
    fun `coroutine name`() {
        runBlocking {
            launch(context = CoroutineName("test")) {
                println("Coroutine name: ${coroutineContext[CoroutineName]}")
                println("Thread name: ${Thread.currentThread().name}")
            }
        }
    }

    @Test
    fun `thread context`() {
        val singleThreadContext = newSingleThreadContext("TestSingleThread")
        val multiThreadContext = newFixedThreadPoolContext(3, "TestMultiThread")

        runBlocking(singleThreadContext) {
            println("Single thread context: ${Thread.currentThread().name}")
            launch {
                println("Single thread context: ${Thread.currentThread().name}")
            }
        }

        runBlocking(multiThreadContext) {
            println("Multi thread context: ${Thread.currentThread().name}")
            launch {
                println("Multi thread context: ${Thread.currentThread().name}")
            }
            launch {
                println("Multi thread context: ${Thread.currentThread().name}")
            }
            launch {
                println("Multi thread context: ${Thread.currentThread().name}")
            }
        }
    }

    @Test
    fun `coroutine dispatcher inherits`() {
        val dispatcher = newFixedThreadPoolContext(3, "InheritDispatcher")
        runBlocking(dispatcher) { // 부모 runBlocking은 Dispatchers.IO를 사용
            println("Parent coroutine running on: ${Thread.currentThread().name}")

            launch {
                // 디스패처를 따로 설정하지 않음 → 부모의 디스패처(IO)를 상속받음
                println("Child coroutine running on: ${Thread.currentThread().name}")
            }
        }

        val parentDispatcher = newFixedThreadPoolContext(3, "InheritParentDispatcher")
        val childDispatcher = newFixedThreadPoolContext(3, "InheritChildDispatcher")

        runBlocking(parentDispatcher) { // 부모 runBlocking은 Dispatchers.IO를 사용
            println("Parent coroutine running on: ${Thread.currentThread().name}")

            launch(childDispatcher) {
                // 디스패처를 따로 설정 → 부모의 디스패처(IO)를 상속받지 않고, 새로운 디스패처(Default)를 사용
                println("Child coroutine running on: ${Thread.currentThread().name}")
            }
        }
    }


    @Test
    fun `cpu bound task with Dispatcher Default and thread`() {
        runBlocking(Dispatchers.Default) {
            val time = measureTimeMillis {
                val jobs = List(4) {
                    async(Dispatchers.Default) {
                        heavyCpuTask()
                    }
                }
                jobs.awaitAll()
            }
            println("Dispatchers.Default took: ${time}ms")
        }

        val threads = mutableListOf<Thread>()
        val startTime = System.currentTimeMillis()
        repeat(4) {
            val thread = Thread {
                heavyCpuTask()
            }
            threads.add(thread)
            thread.start()
        }
        threads.forEach { it.join() }
        val endTime = System.currentTimeMillis()
        println("Raw Threads took: ${endTime - startTime}ms")
    }

    @Test
    fun `limitedParallelism prevents thread starvation`() = runBlocking {
        val limitedDispatcher = Dispatchers.Default.limitedParallelism(1)

        val time = measureTimeMillis {
            val job1 = launch(Dispatchers.Default) {
                // 무제한 디스패처 → 가능한 모든 스레드를 사용하려 함
                println("[default heavy] started on ${Thread.currentThread().name} - started at ${System.currentTimeMillis()}")
                heavyCpuTask()
                println("[default heavy] finished on ${Thread.currentThread().name} - finished at ${System.currentTimeMillis()}")

            }

            val job2 = launch(limitedDispatcher) {
                // 제한된 디스패처 → 다른 연산을 방해하지 않고 실행
                println("[limited safe] started on ${Thread.currentThread().name} - started at ${System.currentTimeMillis()}")
                heavyCpuTask()
                println("[limited safe] finished on ${Thread.currentThread().name} - finished at ${System.currentTimeMillis()}")
            }

            joinAll(job1, job2)
        }

        println("Total time with limitedParallelism: ${time}ms")
    }


    private fun heavyCpuTask(iterations: Int = 10_000_000): Long {
        var sum = 0L
        for (i in 1..iterations) {
            sum += i
        }
        return sum
    }


    @Test
    fun `limitedParallelism can be delayed when all Default threads are busy`() = runBlocking {
        val cpuCores = Runtime.getRuntime().availableProcessors()
        val limitedDispatcher = Dispatchers.Default.limitedParallelism(1)

        println("Available processors: $cpuCores")

        val time = measureTimeMillis {
            val heavyJobs = List(cpuCores) { i ->
                launch(Dispatchers.Default) {
                    cpuIntensiveTask("default-heavy-$i", 2000)
                }
            }

            // 지연 여부 확인용
            val limitedJob = async(limitedDispatcher) {
                val start = System.currentTimeMillis()
                println("[limited-safe] trying to start at ${Thread.currentThread().name} - started at ${start}")
                cpuIntensiveTask("limited-safe", 500)
                val delay = System.currentTimeMillis() - start
                delay
            }

            heavyJobs.joinAll()
            val waitTime = limitedJob.await()
            println("limited-safe waited ~$waitTime ms before executing")
        }

        println("Total test time: ${time}ms")
    }


    private fun cpuIntensiveTask(name: String, durationMs: Long) {
        val start = System.currentTimeMillis()
        while (System.currentTimeMillis() - start < durationMs) {
            // busy loop to simulate real CPU work
        }
        println("[$name] done on ${Thread.currentThread().name} - finished at ${System.currentTimeMillis()}")
    }

}

