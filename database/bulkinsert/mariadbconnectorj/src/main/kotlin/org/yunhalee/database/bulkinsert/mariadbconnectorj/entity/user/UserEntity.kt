package org.yunhalee.database.bulkinsert.mariadbconnectorj.entity.user

import org.yunhalee.database.bulkinsert.mariadbconnectorj.jpa.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.Table


@Entity
@Table(name = "user")
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
