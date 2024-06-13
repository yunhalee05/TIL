package com.yunhalee.webclient

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
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


    @Test
    fun `+가 포함된 문자열을 encoder를 사용하지 않는 요청으로 보냈을 때 제대로 decode 되지 않는다`() {
        val request = TestSearchData(
            name = listOf("+!@#$%%^&*();:'=+,/?[]"),
            age = listOf(20, 30),
            grade = 1
        )
        val map: Map<String, String> = TestSearchData::class.memberProperties.associate { it.name to it.call(request).toString() }
        println(map)
        val response = webclientWithoutUriFactory.getForObject("", mapOf("tets" to "key"), String::class.java, RuntimeException::class.java)
        println(response)
    }


    @Test
    fun `+가 포함된 문자열을 encoder를 사용하고 숫자문자열을 구분하지 않는 요청으로 보냈을 때 일반 숫자가 문자열로 전달된다`() {
        val request = TestSearchData(
            name = listOf("+!@#$%%^&*();:'=+,/?[]"),
            age = listOf(20, 30),
            grade = 1
        )
        val map: Map<String, String> = TestSearchData::class.memberProperties.associate { it.name to it.call(request).toString() }
        val response = webclientWithUriFactory.getForObjectWithEncodedQueryParamsWithoutNumber("", map, String::class.java, RuntimeException::class.java)
        println(response)
    }

    @Test
    fun `+가 포함된 문자열을 encoder를 사용하고 숫자문자열을 구분하는 요청으로 보냈을 때 제대로 decode 된다`() {
        val request = TestSearchData(
            name = listOf("+!@#$%%^&*();:'=+,/?[]"),
            age = listOf(20, 30),
            grade = 1
        )
        val map: Map<String, String> = TestSearchData::class.memberProperties.associate { it.name to it.call(request).toString() }
        val response = webclientWithUriFactory.getForObjectWithEncodedQueryParamsWithNumber("", map, String::class.java, RuntimeException::class.java)
        println(response)
    }
}