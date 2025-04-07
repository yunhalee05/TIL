package org.yunhalee.database.bulkinsert.mariadbconnectorj.user.request

data class DeleteUserRequest(
    val ids: List<Long> = listOf(),
)