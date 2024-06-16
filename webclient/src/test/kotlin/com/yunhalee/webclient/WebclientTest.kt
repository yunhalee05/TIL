package com.yunhalee.webclient

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.util.LinkedMultiValueMap
import java.lang.RuntimeException
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

    @Test
    fun `+가 포함된 문자열을 리스트가 아닌 단일 요청 값으로 {}을 이용해서 요청을 보내면 제대로 전달된다`() {
        val request = TestData(
            name = "+!@#$%%^&*();:'=+,/?[]"
        )
        val map: Map<String, String> = TestData::class.memberProperties
            .filter { it.call(request) != null }
            .associate {
                it.name to it.call(request).toString()
            }
        val response = webclientWithoutUriFactory.getForObjectWithQueryParamWithoutNumber("/test-raw", map, TestData::class.java, RuntimeException::class.java)!!
        assertThat(response.name).isEqualTo(request.name)
    }

    @Test
    fun `+가 포함된 문자열을 리스트가 아닌 단일 요청 값으로 {}을 이용해서 요청을 보낼때, 숫자 데이터도 넘기면 예외가 발생한다`() {
        val request = TestData(
            name = "+!@#$%%^&*();:'=+,/?[]",
            age = 20,
            grade = 1
        )
        val map: Map<String, String> = TestData::class.memberProperties.associate { it.name to it.call(request).toString() }
        val response = webclientWithoutUriFactory.getForObjectWithQueryParamWithoutNumber("/test-raw", map, TestData::class.java, RuntimeException::class.java)!!
        assertThat(response.name).isEqualTo(request.name)
    }

    @Test
    fun `+가 포함된 문자열을 리스트가 아닌 단일 요청 값으로 숫자 요청도 포함해서 보낼때 숫자요청에는 문자값을 그대로, 문자열 요청에는 {}을 이용해서 요청을 보내면 제대로 전달된다`() {
        val request = TestData(
            name = "+!@#$%%^&*();:'=+,/?[]",
            age = 20,
            grade = 1
        )
        val map: Map<String, String> = TestData::class.memberProperties.associate { it.name to it.call(request).toString() }
        val response = webclientWithoutUriFactory.getForObjectWithQueryParamWithNumber("/test-raw", map, TestData::class.java, RuntimeException::class.java)!!
        assertThat(response.name).isEqualTo(request.name)
    }
}
