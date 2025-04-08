package com.yunhalee.database.bulkinsert.mysqlconnectorj.repository.user

import com.yunhalee.database.bulkinsert.mysqlconnectorj.entity.user.UserEntity

interface UserCustomRepository {

    fun saveAllWithBulkInsert(users: List<UserEntity>)

    fun deleteAllWithJdbcTemplate(users: List<UserEntity>)

    fun deleteAllWithInClause(users: List<UserEntity>)
}