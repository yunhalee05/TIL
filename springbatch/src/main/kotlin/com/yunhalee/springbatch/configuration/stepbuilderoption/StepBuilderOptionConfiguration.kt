package com.yunhalee.springbatch.configuration.stepbuilderoption

import com.yunhalee.springbatch.infrastructure.Constants
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager


@Configuration
@ConditionalOnProperty(Constants.PROPERTY_JOB_NAME, havingValue = StepBuilderOptionConfiguration.JOB_NAME)
class StepBuilderOptionConfiguration(
    private val jobRepository: JobRepository
) {

    companion object {
        const val JOB_NAME = "step-builder-option"
    }

    @Bean
    fun job1(step1: Step, step2: Step): Job {
        return JobBuilder("job", jobRepository)
            .start(step1)
            .next(step2)
            .build()
    }


    @Bean
    fun step1(transactionManager: PlatformTransactionManager): Step {
        return StepBuilder("step1", jobRepository)
            .tasklet({ contribution, chunkContext ->
                println(" ============================")
                println(" >> step1 has executed")
                println(" ============================")
                RepeatStatus.FINISHED
            }, transactionManager)
            .allowStartIfComplete(true)
            .build()
    }

    @Bean
    fun step2(transactionManager: PlatformTransactionManager): Step {
        return StepBuilder("step2", jobRepository)
            .tasklet({ _, _ ->
                println(" ============================")
                println(" >> step2 has executed")
                println(" ============================")
                RepeatStatus.FINISHED
            }, transactionManager)
            .startLimit(3)
            .build()
    }

}