package com.yunhalee.webclient

data class TestSearchData(
    val name: List<String>,
    val age: List<Int>,
    val grade: Int
)

data class TestData(
    val name: String,
    val age: Int? = null,
    val grade: Int? = null
)
