package org.yunhalee.database.bulkinsert.mariadbconnectorj.repository.team

import org.yunhalee.database.bulkinsert.mariadbconnectorj.entity.team.TeamEntity

interface TeamCustomRepository {

    fun saveAllWithBulkInsert(teams: List<TeamEntity>)
}