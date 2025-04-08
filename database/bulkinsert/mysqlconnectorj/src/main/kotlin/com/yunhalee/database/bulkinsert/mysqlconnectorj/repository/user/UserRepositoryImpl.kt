package com.yunhalee.database.bulkinsert.mysqlconnectorj.repository.user

import com.yunhalee.database.bulkinsert.mysqlconnectorj.entity.user.UserEntity
import org.springframework.jdbc.core.BatchPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.sql.PreparedStatement
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

    @Transactional
    override fun deleteAllWithJdbcTemplate(users: List<UserEntity>) {
        if (users.isEmpty()) return

        // 방법 1. BatchPreparedStatementSetter 사용 (타입 안정성 높음)
        val sql = "DELETE FROM user WHERE id = ?"
        jdbcTemplate.batchUpdate(sql, object : BatchPreparedStatementSetter {
            override fun setValues(ps: PreparedStatement, i: Int) {
                ps.setLong(1, users[i].id)
            }
            override fun getBatchSize(): Int = users.size
        })

        // 방법 2. 파라미터가 간단한 경우
//        val ids = users.map { arrayOf<Any>(it) }
//        jdbcTemplate.batchUpdate(sql, ids)

    }


    @Transactional
    override fun deleteAllWithInClause(users: List<UserEntity>) {
        if (users.isEmpty()) return

        val ids = users.map { it.id }
        val placeholders = ids.joinToString(",") { "?" }
        val sql = "DELETE FROM user WHERE id IN ($placeholders)"
        jdbcTemplate.update(sql, *ids.toTypedArray())
    }

}