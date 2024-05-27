package com.yunhalee.concurrency_redisson.service.coupon

import com.yunhalee.concurrency_redisson.domain.promotion.Promotion
import com.yunhalee.concurrency_redisson.domain.user.User
import com.yunhalee.concurrency_redisson.repository.coupon.CouponRepository
import com.yunhalee.concurrency_redisson.repository.user.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors
import java.util.concurrent.Future


//@Disabled("동시성 이슈 테스트 코드 입니다")
@SpringBootTest
class CouponServiceTest {

    @Autowired
    private lateinit var sut: CouponService

    @Autowired
    private lateinit var couponRepository: CouponRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    private lateinit var users: List<User>
    private lateinit var promotion: Promotion

    @BeforeEach
    fun setUp() {
        userRepository.saveAll((1..1000).map { index ->
            User(name = "user$index", email = "user$index@test.com", phone = "010-1234-568$index")
        }.toList())
        promotion = Promotion(price = 3000L, startAt = LocalDateTime.now(), endAt = LocalDateTime.now().plusDays(7))
    }


    @DisplayName("발급을 요청하면 쿠폰을 발행한다")
    @Test
    fun `쿠폰을 발급한다`() {
        // when
        sut.issueCoupon(users[0].id, promotion.id)

        // then
        assertEquals(1, couponRepository.count())
    }

    @DisplayName("RedissonLock을 적용하지 않은 케이스 테스트 입니다.")
    @Test
    fun `동시에 쿠폰생성을 요청하면 예정된 쿠폰 수보다 더 많이 발행한다`() {
        // given
        val couponLimit = 100
        val numberOfThreads = 1000
        val executorService = Executors.newFixedThreadPool(numberOfThreads)
//        val futures = mutableListOf<Future<*>>()

        val latch = CountDownLatch(numberOfThreads)
        for (i in 0 until numberOfThreads) {
            val userId = i.toLong()
            executorService.submit {
                try {
                    sut.issueCoupon(userId, promotion.id, couponLimit)
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()
        Thread.sleep(10000)


//        repeat(numberOfThreads) {
//            futures.add(executorService.submit { sut.issueCoupon(1L, promotion.id) })
//        }
//        executorService.shutdown()

//        // then
//        val exception = assertThrows<ExecutionException> {
//            futures.forEach { it.get() }
//        }

        assertThat(couponRepository.countAllByPromotionId(promotionId = promotion.id)).isEqualTo(couponLimit)
 }

    @DisplayName("RedissonLock을 적용하여 동시성 이슈를 해결한 테스트 케이스 입니다.")
    @Test
    fun `동시에 토큰 만료 요청에도 동시성 이슈가 발생하지 않는다`() {
        // given
        val numberOfThreads = 2
        val executorService = Executors.newFixedThreadPool(numberOfThreads)
        val futures = mutableListOf<Future<*>>()

        // when
        repeat(numberOfThreads) {
            futures.add(executorService.submit { sut.issueCoupon(1L, promotion.id) })
        }
        executorService.shutdown()

        // then
        assertDoesNotThrow {
            futures.forEach { it.get() }
        }
    }
}
