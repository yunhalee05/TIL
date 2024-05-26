package com.yunhalee.concurrency_redisson.repository.user

import com.yunhalee.concurrency_redisson.domain.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long>
