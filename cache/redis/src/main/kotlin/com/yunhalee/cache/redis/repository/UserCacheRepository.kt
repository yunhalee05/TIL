package com.yunhalee.cache.redis.repository

import com.yunhalee.cache.redis.configuration.UserCacheConfig
import com.yunhalee.cache.redis.domain.User
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class UserCacheRepository {

    @Cacheable(
        value = [UserCacheConfig.USER_PREFIX_V1],
        key = "#id",
        unless = "#result == null"
    )
    fun findById(id: Long): User? {
        return null
    }


    @Cacheable(
        value = [UserCacheConfig.USER_PREFIX_V1],
        key = "#id"
    )
    fun create(id: Long, name: String, email: String): User {
        return User.of(id, name, email)
    }

    @CachePut(
        value = [UserCacheConfig.USER_PREFIX_V1],
        key = "#id"
    )
    fun updateEmail(user:User, email: String): User {
        user.updateEmail(email)
        return user
    }


    @CacheEvict(
        value = [UserCacheConfig.USER_PREFIX_V1],
        key = "#id"
    )
    fun delete(id: Long) {
    }
}