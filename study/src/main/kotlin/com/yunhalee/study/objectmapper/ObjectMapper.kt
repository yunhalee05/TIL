package com.yunhalee.study.objectmapper

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

object ObjectMapper {

    val objectMapper = ObjectMapper()
        //  (no Creators, like default constructor, exist) 예외 발생 시 다음 옵션 추가
        // jackson은 deserialize할 때 빈 생성자가 필요한데, 코틀린은 빈 생성자를 별도로 만들 수 없으므로 jackson-module-kotlin 의존성을 추가하여 문제를 해결한다.
        .registerKotlinModule()
}