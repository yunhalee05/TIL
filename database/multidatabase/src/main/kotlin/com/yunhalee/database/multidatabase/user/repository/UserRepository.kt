package com.yunhalee.database.multidatabase.user.repository

import com.yunhalee.database.multidatabase.user.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<UserEntity, Long>{
}