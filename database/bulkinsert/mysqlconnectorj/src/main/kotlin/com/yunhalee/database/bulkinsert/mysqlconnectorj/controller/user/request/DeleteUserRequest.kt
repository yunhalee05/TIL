package com.yunhalee.database.bulkinsert.mysqlconnectorj.controller.user.request

data class DeleteUserRequest(
    val ids: List<Long> = listOf(),
)