package com.yunhalee.database.bulkinsert.mysqlconnectorj.entity.user

import com.yunhalee.database.bulkinsert.mysqlconnectorj.util.jpa.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity


@Entity
class UserEntity(
    @Column
    var email: String,

    @Column
    var name: String,

    @Column
    var phone: String,

    @Convert(converter = UserStatusConverter::class)
    @Column
    var status: UserStatus = UserStatus.ACTIVE,
) : BaseEntity()
