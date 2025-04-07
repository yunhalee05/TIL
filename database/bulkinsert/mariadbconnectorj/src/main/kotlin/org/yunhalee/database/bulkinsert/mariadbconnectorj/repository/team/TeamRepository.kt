package org.yunhalee.database.bulkinsert.mariadbconnectorj.repository.team

import org.yunhalee.database.bulkinsert.mariadbconnectorj.entity.team.TeamEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.yunhalee.database.bulkinsert.mariadbconnectorj.repository.team.TeamCustomRepository

interface TeamRepository : JpaRepository<TeamEntity, Long>, TeamCustomRepository {
}