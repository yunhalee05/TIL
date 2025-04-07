package com.yunhalee.database.bulkinsert.mysqlconnectorj.entity.team

import com.yunhalee.database.bulkinsert.mysqlconnectorj.util.jpa.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table


@Entity
@Table(name = "team")
class TeamEntity(
    @Column
    var email: String,
    @Column
    var name: String,
    @Column
    var phone: String,
) : BaseEntity()
