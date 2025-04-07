package org.yunhalee.database.bulkinsert.mariadbconnectorj.repository.user

import org.yunhalee.database.bulkinsert.mariadbconnectorj.entity.user.UserEntity

interface UserCustomRepository {

    fun saveAllWithBulkInsert(users: List<UserEntity>)
}