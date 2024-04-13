package com.yunhalee.springbatch.configuration.executioncontext

import com.yunhalee.springbatch.infrastructure.Constants
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ExecutionContext
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager


@Configuration
@ConditionalOnProperty(Constants.PROPERTY_JOB_NAME, havingValue = ExecutionContextConfiguration.JOB_NAME)
class ExecutionContextConfiguration(
    private val jobRepository: JobRepository
) {

    companion object {
        const val JOB_NAME = "executionContext"
    }

    @Bean
    fun job(step1: Step, step2: Step, step3: Step, step4: Step): Job {
        return JobBuilder("executionContextJob", jobRepository)
            .start(step1)
            .next(step2)
            .next(step3)
            .next(step4)
//            .listener(JobResultListener())
            .build()
    }

    @Bean
    fun step1(transactionManager: PlatformTransactionManager): Step {
        return StepBuilder("step1", jobRepository)
            .tasklet({ _, chunkContext ->
                val jobExecutionContext: ExecutionContext = chunkContext.stepContext.stepExecution.jobExecution.executionContext
                val stepExecutionContext: ExecutionContext = chunkContext.stepContext.stepExecution.executionContext

                val jobName = chunkContext.stepContext.stepExecution.jobExecution.jobInstance.jobName
                val stepName = chunkContext.stepContext.stepExecution.stepName

                if (jobExecutionContext.get("jobName") == null) {
                    jobExecutionContext.put("jobName", jobName)
                }
                if (stepExecutionContext.get("stepName") == null) {
                    stepExecutionContext.put("stepName", stepName)
                }
                println("jobName: " + jobExecutionContext.get("jobName"))
                println("stepName: " + stepExecutionContext.get("stepName"))
                println(" ============================")
                println(" >> step1 has executed")
                println(" ============================")
                RepeatStatus.FINISHED
            }, transactionManager)
            .build()
    }

    @Bean
    fun step2(transactionManager: PlatformTransactionManager): Step {
        return StepBuilder("step2", jobRepository)
            .tasklet({ _, chunkContext ->
                val jobExecutionContext: ExecutionContext = chunkContext.stepContext.stepExecution.jobExecution.executionContext
                val stepExecutionContext: ExecutionContext = chunkContext.stepContext.stepExecution.executionContext
                println("jobName: " + jobExecutionContext.get("jobName"))
                println("stepName: " + stepExecutionContext.get("stepName"))
                val stepName = chunkContext.stepContext.stepExecution.stepName

                if (stepExecutionContext.get("stepName") == null) {
                    stepExecutionContext.put("stepName", stepName)
                }

                println(" ============================")
                println(" >> step2 has executed")
                println(" ============================")
                RepeatStatus.FINISHED
            }, transactionManager)
            .build()
    }


    @Bean
    fun step3(transactionManager: PlatformTransactionManager): Step {
        return StepBuilder("step3", jobRepository)
            .tasklet({ _, chunkContext ->
                val name = chunkContext.stepContext.stepExecution.jobExecution.executionContext["name"]

                if (name == null) {
                    chunkContext.stepContext.stepExecution.jobExecution.executionContext.put("name", "testUser");
                    throw RuntimeException("step3 has failed!!!!!");
                }

                RepeatStatus.FINISHED
            }, transactionManager)
            .build()
    }

    @Bean
    fun step4(transactionManager: PlatformTransactionManager): Step {
        return StepBuilder("step4", jobRepository)
            .tasklet({ _, _ ->
                RepeatStatus.FINISHED
            }, transactionManager)
            .build()
    }


}