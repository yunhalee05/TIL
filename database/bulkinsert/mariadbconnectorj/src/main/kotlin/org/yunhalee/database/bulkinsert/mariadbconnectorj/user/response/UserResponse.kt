package org.yunhalee.database.bulkinsert.mariadbconnectorj.user.response

data class UserResponse(
    val id: Long,
    val email: String,
    val name: String,
    val phone: String,
)