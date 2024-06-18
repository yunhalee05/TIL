package com.yunhalee.study.controller.request

import com.yunhalee.study.domain.State
import com.yunhalee.study.validator.Enum
import jakarta.validation.constraints.NotNull

data class ValidatorRequest(

    @NotNull
    @Enum(enumClass = State::class, ignoreCase = true, excludeUnknown = true, message = "올바르지 상태입니다.", allowValueEquality = true)
    val state: String
)
