package org.yunhalee.database.bulkinsert.mariadbconnectorj.repository.user

import org.yunhalee.database.bulkinsert.mariadbconnectorj.entity.user.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.yunhalee.database.bulkinsert.mariadbconnectorj.repository.user.UserCustomRepository

interface UserRepository: JpaRepository<UserEntity, Long>, UserCustomRepository {
}