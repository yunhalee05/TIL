package com.yunhalee.database.multidatabase.team.repository

import com.yunhalee.database.multidatabase.team.entity.TeamEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TeamRepository : JpaRepository<TeamEntity, Long> {
}