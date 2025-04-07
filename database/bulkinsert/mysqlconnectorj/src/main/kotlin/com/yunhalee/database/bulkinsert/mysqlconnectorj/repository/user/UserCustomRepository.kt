package com.yunhalee.database.bulkinsert.mysqlconnectorj.repository.user

import com.yunhalee.database.bulkinsert.mysqlconnectorj.entity.user.UserEntity

interface UserCustomRepository {

    fun saveAllWithBulkInsert(users: List<UserEntity>)
}