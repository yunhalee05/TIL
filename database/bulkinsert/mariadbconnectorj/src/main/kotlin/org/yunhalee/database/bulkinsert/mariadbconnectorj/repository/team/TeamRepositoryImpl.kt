package org.yunhalee.database.bulkinsert.mariadbconnectorj.repository.team

import org.yunhalee.database.bulkinsert.mariadbconnectorj.entity.team.TeamEntity
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class TeamRepositoryImpl(
    private val jdbcTemplate: JdbcTemplate
) : TeamCustomRepository {

    @Transactional
    override fun saveAllWithBulkInsert(teams: List<TeamEntity>) {
        if (teams.isEmpty()) return
        val sql = """
            INSERT INTO team (email, name, phone)
            VALUES (?, ?, ?)
        """.trimIndent()

        jdbcTemplate.batchUpdate(sql, teams, teams.size) { ps, team ->
            ps.setString(1, team.email)
            ps.setString(2, team.name)
            ps.setString(3, team.phone)
        }
    }
}