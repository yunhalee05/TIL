package com.yunhalee.database.bulkinsert.mysqlconnectorj.controller.user.request

data class CreateUserRequest(
    val email: String,
    val name: String,
    val phone: String,
)