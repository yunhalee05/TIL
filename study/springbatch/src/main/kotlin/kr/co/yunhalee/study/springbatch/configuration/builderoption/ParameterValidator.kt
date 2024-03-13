package kr.co.yunhalee.study.springbatch.configuration.builderoption

import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.JobParametersValidator
import org.springframework.batch.item.validator.Validator

class ParameterValidator : JobParametersValidator {
    override fun validate(parameters: JobParameters?) {
        try {
            parameters?.parameters?.get("name") ?: throw RuntimeException("파라미터 오류입니다.")
        } catch (e: Exception) {
            throw RuntimeException("파라미터 오류입니다.")
        }
    }
}