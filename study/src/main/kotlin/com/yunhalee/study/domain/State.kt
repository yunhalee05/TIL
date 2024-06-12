package com.yunhalee.study.domain

import com.fasterxml.jackson.annotation.JsonCreator

enum class State(val value: String, val description:String)  {
    UNKNOWN("unknown", "알수없음"),
    ACTIVE("active", "활성"),
    INACTIVE("inactive", "비활성");

    companion object {
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
        // Jackson이 @JsonCreator 사용할 때 자바의 static class를 사용하여 companion object를 사용시 Companion Class 안에 함수만 만들어지고 static class가 생성되지 않기 때문에 해당 어노테이션 필요
        @JvmStatic
        fun fromValue(value: String): State =
            entries.find {
                it.value == value
            } ?: UNKNOWN
    }
}