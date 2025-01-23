package com.yunhalee.cache.redis.domain

import java.time.LocalDateTime

data class User(
    val id: Long,
    var name: String,
    var email: String,
    val createdAt: LocalDateTime,
    var updatedAt: LocalDateTime
) {
    companion object {
        fun of(id: Long, name: String, email: String): User {
            val now = LocalDateTime.now()
            return User(id, name, email, now, now)
        }
    }

    fun updateEmail(email: String) {
        this.email = email
        this.updatedAt = LocalDateTime.now()
    }
}