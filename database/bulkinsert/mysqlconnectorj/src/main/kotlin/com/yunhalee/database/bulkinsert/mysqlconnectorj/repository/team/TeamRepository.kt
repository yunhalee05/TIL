package com.yunhalee.database.bulkinsert.mysqlconnectorj.repository.team

import com.yunhalee.database.bulkinsert.mysqlconnectorj.entity.team.TeamEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TeamRepository : JpaRepository<TeamEntity, Long> {
}