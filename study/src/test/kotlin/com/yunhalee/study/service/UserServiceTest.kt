package com.yunhalee.study.service

import com.yunhalee.study.domain.State
import com.yunhalee.study.domain.UserWithState
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class UserServiceTest {

    private val testUserService = mockk<TestUserService>()
    private val sut = UserService(testUserService)

    @Test
    fun getUser() {
        // given
        val userId = 1L
        val expected = UserWithState(
            name = "yunhalee",
            phone = "010-1234-5678",
            age = 30,
            state = State.ACTIVE
        )
        every { testUserService.userWithActiveState() } returns expected
        every { testUserService.userWithInactiveState() } returns UserWithState(
            name = "yunhalee2",
            phone = "010-1234-5679",
            age = 30,
            state = State.INACTIVE
        )

        // when
        val response = sut.getUser(userId)

        // then
        assertThat(response).isEqualTo(expected)
    }
}
