package org.yunhalee.database.bulkinsert.mariadbconnectorj.repository.user

import org.yunhalee.database.bulkinsert.mariadbconnectorj.entity.user.UserEntity
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.sql.Timestamp

@Component
class UserRepositoryImpl(
    private val jdbcTemplate: JdbcTemplate
) : UserCustomRepository {

    @Transactional
    override fun saveAllWithBulkInsert(users: List<UserEntity>) {
        if (users.isEmpty()) return
        val sql = """
            INSERT INTO user (email, name, phone, status, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, ?)
        """.trimIndent()

        jdbcTemplate.batchUpdate(sql, users, users.size) { ps, user ->
            ps.setString(1, user.email)
            ps.setString(2, user.name)
            ps.setString(3, user.phone)
            ps.setString(4, user.status.name)
            ps.setTimestamp(5, Timestamp.valueOf(user.createdAt))
            ps.setTimestamp(6, Timestamp.valueOf(user.updatedAt))
        }
    }

    @Transactional
    override fun saveAllAsMultiRow(users: List<UserEntity>) {
        if (users.isEmpty()) return

        val sqlPrefix = "INSERT INTO user (email, name, phone, status, created_at, updated_at) VALUES "
        val rowPlaceholders = List(users.size) { "(?, ?, ?, ?, ?, ?)" }.joinToString(", ")
        val finalSql = sqlPrefix + rowPlaceholders

        val params = mutableListOf<Any>()
        for (user in users) {
            params += user.email
            params += user.name
            params += user.phone
            params += user.status.name
            params += Timestamp.valueOf(user.createdAt)
            params += Timestamp.valueOf(user.updatedAt)
        }

        jdbcTemplate.update(finalSql, *params.toTypedArray())
    }
}