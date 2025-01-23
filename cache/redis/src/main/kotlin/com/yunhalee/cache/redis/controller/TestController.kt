package com.yunhalee.cache.redis.controller

import com.yunhalee.cache.redis.domain.User
import com.yunhalee.cache.redis.repository.TeamCacheRepository
import com.yunhalee.cache.redis.repository.UserCacheRepository
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/test/v1")
class TestController(
    private val teamCacheRepository: TeamCacheRepository,
    private val userCacheRepository: UserCacheRepository
) {

    @PostMapping("/users")
    fun createUser(@RequestBody user: User): User {
        return userCacheRepository.create(user.id, user.name, user.email)
    }

    @GetMapping("/users/{id}")
    fun getUser(@PathVariable id: Long): User? {
        return userCacheRepository.findById(id)
    }

    @PostMapping("/users/{id}")
    fun updateEmail(@PathVariable id: Long, @RequestBody email: String): User {
        val user = userCacheRepository.findById(id) ?: throw IllegalArgumentException("User not found")
        return userCacheRepository.updateEmail(user, email)
    }

    @DeleteMapping("/users/{id}")
    fun deleteUser(@PathVariable id: Long) {
        userCacheRepository.delete(id)
    }

    @GetMapping("/teams/{id}")
    fun getTeam(@PathVariable id: Long): String? {
        return teamCacheRepository.findById(id)
    }

    @PostMapping("/teams")
    fun createTeam(): String {
        val id = System.currentTimeMillis().toString()
        return teamCacheRepository.create(id)
    }

    @PostMapping("/teams/{id}")
    fun refreshTeam(@PathVariable id: Long): String {
        return teamCacheRepository.refresh(id)
    }

    @DeleteMapping("/teams/{id}")
    fun deleteTeam(@PathVariable id: Long) {
        teamCacheRepository.delete(id)
    }
}