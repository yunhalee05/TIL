package com.yunhalee.study.service

import com.yunhalee.study.domain.UserWithState
import org.springframework.stereotype.Service

@Service
class UserService(
    private val testUserService: TestUserService
) {

    fun getUser(id: Long): UserWithState {
        testUserService.userWithInactiveState()
        return testUserService.userWithActiveState()
    }
}
