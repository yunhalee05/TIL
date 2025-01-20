package com.yunhalee.database.multidatabase.util.jpa

import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

@MappedSuperclass
abstract class BaseEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @CreatedDate
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @LastModifiedDate
    var updatedAt: LocalDateTime = LocalDateTime.now()
)
