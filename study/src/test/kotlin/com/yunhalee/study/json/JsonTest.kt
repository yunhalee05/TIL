package com.yunhalee.study.json

import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.yunhalee.study.domain.UserWithGrade
import com.yunhalee.study.domain.UserWithState
import com.yunhalee.study.objectmapper.ObjectMapper
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class JsonTest {

    private val objectMapper = ObjectMapper.objectMapper

    @Test
    fun `여러 인자를 가진 enum 클래스에서 옵션 없이 JsonCreator를 사용시에 예외가 발생한다`() {
        val json = """
            {
                "name": "yunhalee",
                "phone": "010-1234-5678",
                "age": 20,
                "grade": "bronze"
            }
        """.trimIndent()

        assertThatThrownBy { objectMapper.readValue(json, UserWithGrade::class.java) }
            .isInstanceOf(MismatchedInputException::class.java)
            .hasMessageContaining(" expects JSON Object (JsonToken.START_OBJECT), got JsonToken.VALUE_STRING")

//        {
//            "name": "yunhalee",
//            "phone": "010-1234-5678",
//            "age": 20,
//            "grade": "bronze",
//            "state": "active"
//        }
    }

    @Test
    fun `여러 인자를 가진 enum 클래스에서 DELEGATING 옵션으로 JsonCreator를 사용시에 역직렬화에 성공한다`() {
        val json = """
            {
                "name": "yunhalee",
                "phone": "010-1234-5678",
                "age": 20,
                "state": "active"
            }
        """.trimIndent()

        val user = objectMapper.readValue(json, UserWithState::class.java)

        println(user)
    }
}
