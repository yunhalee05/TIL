package com.yunhalee.springbatch.configuration.decider

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
@ConditionalOnProperty(Constants.PROPERTY_JOB_NAME, havingValue = DeciderConfiguration.JOB_NAME)
class DeciderConfiguration(
    private val jobRepository: JobRepository,
    private val decider: Decider
) {

    companion object {
        const val JOB_NAME = "decider"
    }


    @Bean
    fun job(startStep: Step, oddStep: Step, evenStep: Step): Job {
        return JobBuilder("job", jobRepository)
            .start(startStep)
            .next(decider)
            .from(decider).on("ODD").to(oddStep)
            .from(decider).on("EVEN").to(evenStep)
            .end()
            .build()
    }

    @Bean
    fun startStep(transactionManager: PlatformTransactionManager): Step {
        return StepBuilder("startStep", jobRepository)
            .tasklet({ _, _ ->
                println(" ============================")
                println(" >> startStep has executed")
                println(" ============================")
                RepeatStatus.FINISHED
            }, transactionManager)
            .build()
    }

    @Bean
    fun evenStep(transactionManager: PlatformTransactionManager): Step {
        return StepBuilder("evenStep", jobRepository)
            .tasklet({ _, _ ->
                println(" ============================")
                println(" >> evenStep has executed")
                println(" ============================")
                RepeatStatus.FINISHED
            }, transactionManager)
            .build()
    }

    @Bean
    fun oddStep(transactionManager: PlatformTransactionManager): Step {
        return StepBuilder("oddStep", jobRepository)
            .tasklet({ _, _ ->
                println(" ============================")
                println(" >> oddStep has executed")
                println(" ============================")
                RepeatStatus.FINISHED
            }, transactionManager)
            .build()
    }

}