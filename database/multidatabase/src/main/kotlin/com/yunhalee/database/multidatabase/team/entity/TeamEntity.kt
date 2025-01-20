package com.yunhalee.database.multidatabase.team.entity

import com.yunhalee.database.multidatabase.util.jpa.BaseEntity
import jakarta.persistence.Entity


@Entity
class TeamEntity(
    var email: String,
    var name: String,
    var phone: String,
) : BaseEntity()
