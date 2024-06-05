package com.yunhalee.concurrency_redisson.mysql

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset

@SpringBootTest
class DatetimeTest {


    @Autowired
    private lateinit var testRepository: TestRepository


    @Test
    @Rollback(false)
    fun test() {
        // 2024년 June 5일 Wednesday PM 1:28:50.783
        val beforeInstant = Instant.ofEpochMilli(1717594130783)
        val time = beforeInstant.atZone(ZoneOffset.UTC).toLocalDateTime()

        // 2024년 June 5일 Wednesday PM 1:28:50.883
        val afterInstant = Instant.ofEpochMilli(1717594130883)

        val test = testRepository.findById(testRepository.save(Test(time = time)).id).get()
        println(test.time)
        // 테스트 케이스 성공 => before 시간이 after 시간보다 이전인 것으로 판단이 됨
        assertFalse(test.isAfter(afterInstant.atZone(ZoneOffset.UTC).toLocalDateTime()))
    }

    @Test
    @Rollback(false)
    fun test2() {
        // 2024년 June 5일 Wednesday PM 11:59:59.999999999
        val beforeTime = LocalDate.of(2024, 6, 5).atTime(LocalTime.MAX)

        // 2024년 June 6일 Wednesday AM 12:00:00.000000000
        val afterTime = LocalDate.of(2024, 6, 6).atTime(LocalTime.MIN)

        val test = testRepository.findById(testRepository.save(Test(time = beforeTime)).id).get()
        // 2024-06-06T00:00
        println(test.time)
        // before 시간이 after 시간과 동일한 것으로 판단됨
        assertEquals(test.time, afterTime)
    }


    @Test
    @Rollback(false)
    fun test3() {
        // 2024년 June 5일 Wednesday PM 11:59:59.999999999
        val beforeTime: LocalDateTime = LocalDate.of(2024, 6, 5).atTime(LocalTime.MAX)

        // 2024년 June 6일 Wednesday AM 12:00:00.000000000
        val afterTime: LocalDateTime = LocalDate.of(2024, 6, 6).atTime(LocalTime.MIN)

        testRepository.saveAll(listOf(
            // 2024-06-05 23:59:59.999999
            Test(time = beforeTime.minusNanos(999)),
            // 2024-06-06 00:00:00.000000
            Test(time = afterTime)
        ))
        println(beforeTime)

        val results = testRepository.findAllByTimeIsLessThanEqual(beforeTime)
        results.forEach { println(it.time) }
        // 테스트 실패 : 결과에 after 타임이 포함되어 검색됨
        assertThat(results).hasSize(1)
    }
}