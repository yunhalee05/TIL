package org.yunhalee.database.bulkinsert.mariadbconnectorj.user.request

data class CreateUserRequest(
    val email: String,
    val name: String,
    val phone: String,
)