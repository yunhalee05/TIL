package com.yunhalee.database.multidatabase.user.entity

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

enum class UserStatus {
    ACTIVE,
    INACTIVE
}

@Converter(autoApply = true)
class UserStatusConverter : AttributeConverter<UserStatus, String> {
    override fun convertToDatabaseColumn(attribute: UserStatus): String {
        return attribute.name
    }

    override fun convertToEntityAttribute(dbData: String): UserStatus {
        return UserStatus.values().find { it.name == dbData } ?: UserStatus.INACTIVE
    }
}