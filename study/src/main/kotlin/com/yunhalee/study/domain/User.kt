package com.yunhalee.study.domain

data class UserWithGrade(
    val name: String,
    val phone: String,
    val age: Int,
    val grade: Grade
)

data class UserWithState(
    val name: String,
    val phone: String,
    val age: Int,
    val state: State
)
