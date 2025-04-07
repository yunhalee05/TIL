package com.yunhalee.database.bulkinsert.mysqlconnectorj.repository.user

import com.yunhalee.database.bulkinsert.mysqlconnectorj.entity.user.UserEntity
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.sql.Timestamp
import java.time.ZoneId
import java.time.ZoneOffset

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
}