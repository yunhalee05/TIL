package com.yunhalee.database.bulkinsert.mysqlconnectorj.repository.team

import com.yunhalee.database.bulkinsert.mysqlconnectorj.entity.team.TeamEntity

interface TeamCustomRepository {

    fun saveAllWithBulkInsert(teams: List<TeamEntity>)
}