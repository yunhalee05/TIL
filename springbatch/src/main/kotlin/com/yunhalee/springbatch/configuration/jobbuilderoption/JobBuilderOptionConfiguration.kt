package com.yunhalee.springbatch.configuration.jobbuilderoption

import com.yunhalee.springbatch.infrastructure.Constants
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.ExitStatus
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
@ConditionalOnProperty(Constants.PROPERTY_JOB_NAME, havingValue = JobBuilderOptionConfiguration.JOB_NAME)
class JobBuilderOptionConfiguration(
    private val jobRepository: JobRepository
) {

    companion object {
        const val JOB_NAME = "job-builder-option"
    }

    @Bean
    fun job1(step1: Step, step2: Step): Job {
        return JobBuilder("withoutFlowJob", jobRepository)
//            .incrementer(RunIdIncrementer())
            .incrementer(JobIncrementer())
            .validator(ParameterValidator())
            .preventRestart()
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
                chunkContext.stepContext.stepExecution.status = BatchStatus.FAILED
                contribution.exitStatus = ExitStatus.STOPPED
                RepeatStatus.FINISHED
            }, transactionManager)
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
            .build()
    }

}