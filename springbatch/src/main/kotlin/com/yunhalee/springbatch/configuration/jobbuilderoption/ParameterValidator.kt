package com.yunhalee.springbatch.configuration.jobbuilderoption

import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.JobParametersValidator

class ParameterValidator : JobParametersValidator {
    override fun validate(parameters: JobParameters?) {
        try {
            parameters?.parameters?.get("name") ?: throw RuntimeException("파라미터 오류입니다.")
        } catch (e: Exception) {
            throw RuntimeException("파라미터 오류입니다.")
        }
    }
}