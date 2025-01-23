package com.yunhalee.cache.redis.repository

import com.yunhalee.cache.redis.configuration.RedisConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class TeamCacheRepository {

    @Cacheable(
        value = [RedisConfig.TEAM_PREFIX_V1],
        key = "#id",
        unless = "#result == null"
    )
    fun findById(id: Long): String? {
        return null
    }

    @Cacheable(
        value = [RedisConfig.TEAM_PREFIX_V1],
        key = "#id"
    )
    fun create(id: String): String {
        return UUID.randomUUID().toString()
    }

    @CachePut(
        value = [RedisConfig.TEAM_PREFIX_V1],
        key = "#id"
    )
    fun refresh(id: Long): String {
        return UUID.randomUUID().toString()
    }

    @CacheEvict(
        value = [RedisConfig.TEAM_PREFIX_V1],
        key = "#id"
    )
    fun delete(id: Long) {
    }
}