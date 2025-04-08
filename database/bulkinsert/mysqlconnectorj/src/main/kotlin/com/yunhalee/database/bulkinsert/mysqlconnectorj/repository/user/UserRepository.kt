package com.yunhalee.database.bulkinsert.mysqlconnectorj.repository.user

import com.yunhalee.database.bulkinsert.mysqlconnectorj.entity.user.UserEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<UserEntity, Long>, UserCustomRepository{
    fun findAllByOrderByIdAsc(pageable: Pageable): List<UserEntity>
}