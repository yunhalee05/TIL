package org.yunhalee.database.bulkinsert.mariadbconnectorj.user.request

import org.yunhalee.database.bulkinsert.mariadbconnectorj.user.request.CreateUserRequest

data class CreateUsersRequest(
    val requests: List<CreateUserRequest>
)