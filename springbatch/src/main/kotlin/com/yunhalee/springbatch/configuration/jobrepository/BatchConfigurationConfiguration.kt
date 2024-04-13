package com.yunhalee.springbatch.configuration.jobrepository

import com.yunhalee.springbatch.infrastructure.Constants
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
@ConditionalOnProperty(Constants.PROPERTY_JOB_NAME, havingValue = BatchConfigurationConfiguration.JOB_NAME)
@EnableBatchProcessing(
    dataSourceRef = "batchDataSource",
    transactionManagerRef = "batchTransactionManager",
    maxVarCharLength = 1000,
    isolationLevelForCreate = "ISOLATION_SERIALIZABLE"
)
class BatchConfigurationConfiguration(
    private val jobRepository: JobRepository,
    private val jobRepositoryListener: JobRepositoryListener
) {

    companion object {
        const val JOB_NAME = "batchConfigurer"
    }

    @Bean
    fun job(step1: Step, step2: Step): Job {
        return JobBuilder("job", jobRepository)
            .start(step1)
            .next(step2)
            .listener(jobRepositoryListener)
            .build()
    }

    @Bean
    fun step1(batchTransactionManager: PlatformTransactionManager): Step {
        return StepBuilder("step1", jobRepository)
            .tasklet({ _, _ ->
                println(" ============================")
                println(" >> step1 has executed")
                println(" ============================")
                RepeatStatus.FINISHED
            }, batchTransactionManager)
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