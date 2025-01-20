package com.yunhalee.database.multidatabase.user.entity

import com.yunhalee.database.multidatabase.util.jpa.BaseEntity
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
    var status: UserStatus
) : BaseEntity()
