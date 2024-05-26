package com.yunhalee.concurrency_redisson.domain.user

import com.yunhalee.concurrency_redisson.infrastructure.jpa.BaseEntity
import jakarta.persistence.Entity

@Entity
class User(
    var email: String,
    var name: String,
    var phone: String,
) : BaseEntity()
