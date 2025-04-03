package com.yunhalee.database.bulkinsert.mysqlconnectorj.entity.team

import com.yunhalee.database.bulkinsert.mysqlconnectorj.util.jpa.BaseEntity
import jakarta.persistence.Entity


@Entity
class TeamEntity(
    var email: String,
    var name: String,
    var phone: String,
) : BaseEntity()
