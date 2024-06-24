package com.yunhalee.study.maven

import com.yunhalee.common.util.toInstant
import com.yunhalee.common.util.toLocalDateTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class MavenImportTest {

    @Test
    fun test() {
        val time = LocalDateTime.now()
        val instant = time.toInstant()
        assertThat(time).isEqualTo(instant.toLocalDateTime())
    }
}