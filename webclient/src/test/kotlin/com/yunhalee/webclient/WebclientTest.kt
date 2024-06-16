package com.yunhalee.webclient

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.util.LinkedMultiValueMap
import java.lang.RuntimeException
import java.net.URLEncoder
import kotlin.reflect.full.memberProperties

@SpringBootTest
class WebclientTest {

    @Autowired
    @Qualifier("webclientWithUriFactory")
    private lateinit var webclientWithUriFactory: ApiClient

    @Autowired
    @Qualifier("webclientWithoutUriFactory")
    private lateinit var webclientWithoutUriFactory: ApiClient


    private val data = TestSearchData(
        name = listOf("+!@#$%%^&*();:'=+,/?[]", "testName"),
        age = listOf(20, 30),
        grade = 1
    )


    @Test
    fun `+가 포함된 문자열을 encoder를 사용하지 않는 요청으로 보냈을 때 제대로 decode 되지 않는다`() {
        val request = LinkedMultiValueMap<String, String>()
        data.name.forEach { request.add("name", it) }
        data.age.forEach { request.add("age", it.toString()) }
        request.add("grade", data.grade.toString())
        val response = webclientWithoutUriFactory.getForObject("/test", request, TestSearchData::class.java, RuntimeException::class.java)!!
        assertThat(response.name.first()).isNotEqualTo(data.name[0])
        // + 값이 빈 공백으로 대체된다.
        assertThat(response.name.first()).isEqualTo(data.name[0].replace("+", " "))
    }

    @Test
    fun `+가 포함된 문자열은 encoder를 사용하는 요청으로 보냈을 때 제대로 decode 된다`() {
        val request = LinkedMultiValueMap<String, String>()
        data.name.forEach { request.add("name", it) }
        data.age.forEach { request.add("age", it.toString()) }
        request.add("grade", data.grade.toString())
        val response = webclientWithUriFactory.getForObjectWithUrlEncoder("/test", request, TestSearchData::class.java, RuntimeException::class.java)!!
        assertThat(response.name.first()).isEqualTo(data.name[0])
    }


    @Test
    fun `+가 포함된 문자열을 encode factory를 사용하지 않고 {} 문자열을 사용해 원본으로 보냈을 때 ,가 포함되지 않으면 제대로 decode 된다`() {
        val request = TestSearchData(
            name = listOf("+!@#$%%^&*();:'=+/?[]"),
            age = listOf(20, 30),
            grade = 1
        )
        val map: Map<String, String> = TestSearchData::class.memberProperties.associate {
            val value = it.call(request)
            it.name to if (value is List<*>) value.joinToString(",") else value.toString()
        }
        val response = webclientWithoutUriFactory.getForObjectWithEncodedQueryParamsWithoutNumber("/test", map, TestSearchData::class.java, RuntimeException::class.java)!!
        assertThat(response.name.first()).isEqualTo(data.name[0].replace(",", ""))
    }

    @Test
    fun `+가 포함된 문자열을 {} 문자열을 사용해 원본으로 보내도록 하고 encoder를 사용하고 숫자문자열을 구분하는 요청으로 보냈을 때 , 가 포함되면 제대로 decode 하지 못한다`() {
        val request = TestSearchData(
            name = listOf("+!@#$%%^&*();:'=+,/?[]"),
            age = listOf(20, 30),
            grade = 1
        )
        val map: Map<String, String> = TestSearchData::class.memberProperties.associate {
            val value = it.call(request)
            it.name to if (value is List<*>) value.joinToString(",") else value.toString()
        }
        val response = webclientWithoutUriFactory.getForObjectWithEncodedQueryParamsWithNumber("/test", map, TestSearchData::class.java, RuntimeException::class.java)!!
        assertThat(response.name.first()).isEqualTo(data.name[0].substringBefore(","))
    }
}