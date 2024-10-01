package com.yunhalee.concurrency_redisson.service.user

import com.yunhalee.concurrency_redisson.component.ExecuteWithLockV2
import com.yunhalee.concurrency_redisson.infrastructure.annotation.ExecuteWithLock
import com.yunhalee.concurrency_redisson.repository.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.TimeUnit

@Service
class UserService(
    private val userRepository: UserRepository
) {

    @Transactional
    @ExecuteWithLock(key = "#userId", waitTimeAmount = 5, leaseTimeAmount = 5, timeUnit = TimeUnit.SECONDS)
    fun deleteUser(userId: Long) {
        userRepository.findById(userId).ifPresent {
            println("delete user: ${it.id}")
            Thread.sleep(3)
            userRepository.delete(it)
        }
    }

    @Transactional
    @ExecuteWithLockV2(key = "#userId", waitTimeAmount = 5, leaseTimeAmount = 5, timeUnit = TimeUnit.SECONDS)
    fun deleteUserV2(userId: Long) {
        userRepository.findById(userId).ifPresent {
            println("delete user: ${it.id}")
            Thread.sleep(3)
            userRepository.delete(it)
        }
    }
}
