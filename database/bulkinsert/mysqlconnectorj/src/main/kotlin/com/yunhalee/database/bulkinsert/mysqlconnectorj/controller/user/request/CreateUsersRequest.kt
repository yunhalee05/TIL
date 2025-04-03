package com.yunhalee.database.bulkinsert.mysqlconnectorj.controller.user.request

data class CreateUsersRequest(
    val requests: List<CreateUserRequest>
)