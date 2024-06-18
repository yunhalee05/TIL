package com.yunhalee.study.validator

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class EnumValidator : ConstraintValidator<Enum?, String?> {
    private var annotation: Enum? = null
    private val unknowns = listOf("UNKNOWN", "UNRECOGNIZED", "UNDEFINED", "-1")

    override fun initialize(constraintAnnotation: Enum?) {
        annotation = constraintAnnotation
    }

    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        if (value == null) {
            return annotation!!.nullable
        }
        val enumValues = annotation!!.enumClass.java.enumConstants
        if (enumValues != null) {
            for (enumValue in enumValues) {
                if (value == enumValue.toString() || annotation!!.ignoreCase && value.equals(enumValue.toString(), ignoreCase = true)) {
                    if (annotation!!.excludeUnknown && unknowns.contains(enumValue.toString())) {
                        return false
                    }
                    return true
                }

                if (annotation!!.allowValueEquality && hasValueProperty(enumValue) && value == getValueProperty(enumValue)) {
                    if (annotation!!.excludeUnknown && unknowns.contains(enumValue.toString())) {
                        return false
                    }
                    return true
                }
            }
        }
        return false
    }

    private fun hasValueProperty(enumValue: kotlin.Enum<*>): Boolean {
        return try {
            enumValue.javaClass.getDeclaredField("value")
            true
        } catch (e: NoSuchMethodException) {
            false
        }
    }

    private fun getValueProperty(enumValue: kotlin.Enum<*>): String {
        val valueField = enumValue.javaClass.getDeclaredField("value")
        valueField.isAccessible = true
        return valueField.get(enumValue).toString()
    }
}
