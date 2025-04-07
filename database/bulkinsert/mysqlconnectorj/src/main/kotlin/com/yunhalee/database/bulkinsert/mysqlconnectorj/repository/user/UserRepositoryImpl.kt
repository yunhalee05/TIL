package com.yunhalee.database.bulkinsert.mysqlconnectorj.repository.user

import com.yunhalee.database.bulkinsert.mysqlconnectorj.entity.user.UserEntity
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UserRepositoryImpl(
    private val jdbcTemplate: JdbcTemplate
) : UserCustomRepository {

    @Transactional
    override fun saveAllWithBulkInsert(users: List<UserEntity>) {
        if (users.isEmpty()) return
        val sql = """
            INSERT INTO user (email, name, phone, status)
            VALUES (?, ?, ?, ?)
        """.trimIndent()

        jdbcTemplate.batchUpdate(sql, users, users.size) { ps, team ->
            ps.setString(1, team.email)
            ps.setString(2, team.name)
            ps.setString(3, team.phone)
            ps.setString(4, team.status.name)
        }
    }
}