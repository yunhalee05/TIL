package com.yunhalee.concurrency_redisson.service.coupon

import com.yunhalee.concurrency_redisson.domain.promotion.Promotion
import com.yunhalee.concurrency_redisson.domain.user.User
import com.yunhalee.concurrency_redisson.repository.coupon.CouponRepository
import com.yunhalee.concurrency_redisson.repository.user.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
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
        users = userRepository.saveAll((1..1000).map { index ->
            User(name = "user$index", email = "user$index@test.com", phone = "010-1234-568$index")
        }.toList())
        promotion = Promotion(price = 3000L, startAt = LocalDateTime.now(), endAt = LocalDateTime.now().plusDays(7))
    }


    @DisplayName("정상 쿠폰 발급 로직 테스트")
    @Test
    fun `쿠폰을 발급한다`() {
        // when
        sut.issueCoupon(users[0].id, promotion.id)

        // then
        assertEquals(1, couponRepository.count())
    }

    @DisplayName("정상 쿠폰 발급 로직 테스트")
    @Test
    fun `동일한 사용자가 동일한 프로모션에 대해 중복 발급을 요청하면 예외를 발생시킨다")`() {
        // given
        sut.issueCoupon(users[0].id, promotion.id)

        // when / then
        assertThatThrownBy { sut.issueCoupon(users[0].id, promotion.id) }
            .isInstanceOf(RuntimeException::class.java)
            .hasMessage("해당 프로모션 쿠폰은 한사람 당 하나만 발행이 가능합니다. 이미 발급된 쿠폰이 존재합니다.")
    }

    @DisplayName("정상 쿠폰 발급 로직 테스트")
    @Test
    fun `쿠폰 발급의 갯수가 쿠폰 발행 제한 갯수를 넘으면, 예외가 발생한다`() {
        // given
        val limit = 100
        (0..99).forEach { index ->
            sut.issueCoupon(users[index].id, promotion.id, limit)
        }

        // when / then
        assertThatThrownBy { sut.issueCoupon(users[100].id, promotion.id, limit) }
            .isInstanceOf(RuntimeException::class.java)
            .hasMessage("이미 발급 가능한 쿠폰 갯수가 소진되었습니다.")
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

        assertThat(couponRepository.countAllByPromotionId(promotionId = promotion.id)).isGreaterThan(couponLimit)
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
