package org.yunhalee.database.bulkinsert.mariadbconnectorj.entity.team

import org.yunhalee.database.bulkinsert.mariadbconnectorj.jpa.BaseEntity
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
