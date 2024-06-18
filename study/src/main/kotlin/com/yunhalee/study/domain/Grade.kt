package com.yunhalee.study.domain

import com.fasterxml.jackson.annotation.JsonCreator

enum class Grade(val value: String, val description: String) {
    UNKNOWN("unknown", "알수없음"),
    BRONZE("bronze", "브론즈"),
    SILVER("silver", "실버"),
    GOLD("gold", "골드"),
    PLATINUM("platinum", "플래티넘"),
    DIAMOND("diamond", "다이아몬드");

    companion object {
        @JsonCreator
        @JvmStatic
        fun fromValue(value: String): Grade =
            Grade.values().find {
                it.value == value
            } ?: UNKNOWN
    }
}
