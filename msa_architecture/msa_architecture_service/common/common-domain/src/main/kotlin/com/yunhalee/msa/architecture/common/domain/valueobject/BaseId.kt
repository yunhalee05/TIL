package com.yunhalee.msa.architecture.common.domain.valueobject

import java.util.Objects

abstract class BaseId<T>(
    private val value: T
) {
    fun getValue(): T {
        return value
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val baseId = other as BaseId<*>
        return value == baseId.value
    }

    override fun hashCode(): Int {
        return Objects.hash(value)
    }
}