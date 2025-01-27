package com.yunhalee.msa.architecture.common.domain.entity

import java.util.Objects
import java.util.UUID

abstract class BaseEntity<ID> {
    var id: ID? = null
        private set

    fun setId(id: ID) {
        this.id = id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        val that = other as BaseEntity<*>
        return id == that.id
    }

    override fun hashCode(): Int {
        return Objects.hash(id)
    }
}

