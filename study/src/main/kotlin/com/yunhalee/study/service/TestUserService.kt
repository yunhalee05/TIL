package com.yunhalee.study.service

import com.yunhalee.study.domain.State
import com.yunhalee.study.domain.UserWithState
import org.springframework.stereotype.Service

@Service
class TestUserService {

    fun userWithActiveState() = UserWithState(
        name = "yunhalee",
        phone = "010-1234-5678",
        age = 30,
        state = State.ACTIVE
    )

    fun userWithInactiveState() = UserWithState(
        name = "yunhalee",
        phone = "010-1234-5678",
        age = 30,
        state = State.INACTIVE
    )
}
