package com.yunhalee.concurrency_redisson.service.coupon

import com.yunhalee.concurrency_redisson.domain.coupon.Coupon
import com.yunhalee.concurrency_redisson.domain.promotion.Promotion
import com.yunhalee.concurrency_redisson.domain.user.User
import com.yunhalee.concurrency_redisson.repository.coupon.CouponRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import java.time.LocalDateTime
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors
import java.util.concurrent.Future


//@Disabled("동시성 이슈 테스트 코드 입니다")
@SpringBootTest
@Import(ExecuteWithLockAspect::class)
class CouponServiceTest {

    @Autowired
    private lateinit var sut: CouponService

    @Autowired
    private lateinit var couponRepository: CouponRepository

    private lateinit var users: List<User>
    private lateinit var promotion: Promotion

    @BeforeEach
    fun setUp() {
        users = listOf(
            User(name = "user1", email = "user1@test.com", phone = "010-1234-5678"),
            User(name = "user2", email = "user2@test.com", phone = "010-1234-5679"),
            User(name = "user3", email = "user3@test.com", phone = "010-1234-5680"),
            User(name = "user4", email = "user4@test.com", phone = "010-1234-5681"),
            User(name = "user5", email = "user5@test.com", phone = "010-1234-5682")
        )

        promotion = Promotion(price = 3000L, startAt = LocalDateTime.now(), endAt = LocalDateTime.now().plusDays(7))
    }

    @DisplayName("RedissonLock을 적용하지 않은 케이스 테스트 입니다.")
    @Test
    fun `동시에 쿠폰생성을 요청하면 쿠폰 수를 더 많이 발행한다`() {
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
        val exception = assertThrows<ExecutionException> {
            futures.forEach { it.get() }
        }
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
