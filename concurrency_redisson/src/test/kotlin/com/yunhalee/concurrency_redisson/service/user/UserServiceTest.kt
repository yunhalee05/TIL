package com.yunhalee.concurrency_redisson.service.user

import com.yunhalee.concurrency_redisson.domain.user.User
import com.yunhalee.concurrency_redisson.repository.user.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.OptimisticLockingFailureException
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors
import java.util.concurrent.Future

// @Import(ExecuteWithLockAspect::class)
@SpringBootTest
class UserServiceTest {

    @Autowired
    private lateinit var sut: UserService

    @Autowired
    private lateinit var userRepo: UserRepository

    private lateinit var user: User

    @BeforeEach
    fun setUp() {
        user = userRepo.save(User("test@gmail.com", "홍길동", "010-1234-5678"))
    }

    @DisplayName("RedissonLock을 적용하지 않은 케이스 테스트 입니다.")
    @Test
    fun `동시에 사용자 삭제 요청하면 OptimisticLockingFailureException이 발생한다`() {
        // given
        val numberOfThreads = 2
        val executorService = Executors.newFixedThreadPool(numberOfThreads)
        val futures = mutableListOf<Future<*>>()

        // when
        repeat(numberOfThreads) {
            futures.add(executorService.submit { sut.deleteUser(user.id) })
        }
        executorService.shutdown()

        // then
        val exception = assertThrows<ExecutionException> {
            futures.forEach { it.get() }
        }
        assertTrue(exception.cause is OptimisticLockingFailureException)
        println("--------------------stacktrace-------------")
        println(exception.printStackTrace())
        assertTrue(exception.message!!.contains("Row was updated or deleted by another transaction (or unsaved-value mapping was incorrect)"))
    }

    @DisplayName("RedissonLock을 적용하여 동시성 이슈를 해결한 테스트 케이스 입니다.")
    @Test
    fun `동시에 유저 삭제 요청에도 동시성 이슈가 발생하지 않는다`() {
        // given
        val numberOfThreads = 2
        val executorService = Executors.newFixedThreadPool(numberOfThreads)
        val futures = mutableListOf<Future<*>>()

        // when
        repeat(numberOfThreads) {
            futures.add(executorService.submit { sut.deleteUserV2(user.id) })
        }
        executorService.shutdown()

        // then
        assertDoesNotThrow {
            futures.forEach { it.get() }
        }
    }
}
