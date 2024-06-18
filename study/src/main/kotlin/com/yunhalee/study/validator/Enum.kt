package com.yunhalee.study.validator

import jakarta.validation.Constraint
import jakarta.validation.Payload
import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import kotlin.Enum
import kotlin.reflect.KClass

@Documented
@Target(AnnotationTarget.FIELD, AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.PROPERTY_SETTER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = [EnumValidator::class])
annotation class Enum(
    val message: String = "올바르지 않은 enum 값입니다.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
    val enumClass: KClass<out Enum<*>>,
    val ignoreCase: Boolean = false,
    val excludeUnknown: Boolean = false,
    val allowValueEquality: Boolean = false,
    val nullable: Boolean = false
)
