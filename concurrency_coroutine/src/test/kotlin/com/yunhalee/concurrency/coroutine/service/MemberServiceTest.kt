package com.yunhalee.concurrency.coroutine.service

import com.yunhalee.concurrency.coroutine.domain.Member
import com.yunhalee.concurrency.coroutine.repository.MemberRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.support.TransactionTemplate
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@SpringBootTest
class MemberServiceTest {

    @Autowired
    private lateinit var memberService: MemberService

    @Autowired
    private lateinit var memberRepository: MemberRepository

    private lateinit var member: Member

    @Autowired
    private lateinit var transactionTemplate: TransactionTemplate

//    @BeforeEach
//    fun setUp() {
//        member = memberRepository.save(Member(1, "testUser3"))
//    }

    @Test
    fun test() {
//        val job1 = GlobalScope.async { memberService.updateName(member.id, "updatedName") }
//
//        // 두 번째 코루틴 시작
//        val job2 = GlobalScope.async {  memberService.delete(member.name) }
//
//
//        // job1과 job2의 완료를 기다림
//        runBlocking {
//            job1.await()
//            job2.await()
//        }

        runBlocking {
            val a = async {
                memberService.updateName(member.id, "updatedName")
            }
            val b = async {
                memberService.delete(member.name)
            }

            awaitAll(a, b)
        }
//
//        runBlocking {
//            val a = async {
//                println("12837984")
//                delay(200)
//                println("postlsdkfjlisdjf")
//            }
//
//            val b = async {
//                println("test")
//                delay(300)
//                println("post2")
//            }
//
//            println("$a + $b")
//        }
    }

    @Test
    fun test2() {
// 두 개의 트랜잭션을 병렬로 실행
        val deleteThread = Thread {
            transactionTemplate.execute {
                memberService.delete2(member.name)
            }
        }

        val updateThread = Thread {
            transactionTemplate.execute {
                memberService.updateName2(member.id, "updatedName")
            }
        }

        deleteThread.start() // delete가 먼저 시작되도록 변경
        updateThread.start() // update가 그 다음에 시작되도록 변경

// 두 개의 스레드가 종료될 때까지 대기
        deleteThread.join()
        updateThread.join()
    }

    //    @Throws(InterruptedException::class)
    @Test
    fun test3() {
        member = memberRepository.save(Member(1, "testUser4"))
        memberRepository.save(member)
        val executor = Executors.newFixedThreadPool(2)
        val latch = CountDownLatch(2)
        executor.submit {
            memberService.delete2(member.name)
            latch.countDown()
        }
        executor.submit {
            memberService.updateName2(member.id, "updatedName")
            latch.countDown()
        }
        latch.await()
    }
}
