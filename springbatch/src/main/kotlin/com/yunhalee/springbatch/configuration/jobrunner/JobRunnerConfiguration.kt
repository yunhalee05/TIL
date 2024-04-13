package com.yunhalee.springbatch.configuration.jobrunner

import com.yunhalee.springbatch.infrastructure.Constants
import com.yunhalee.springbatch.listener.JobResultListener
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobInstance
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
@ConditionalOnProperty(Constants.PROPERTY_JOB_NAME, havingValue = JobRunnerConfiguration.JOB_NAME)
class JobRunnerConfiguration(
    private val jobRepository: JobRepository
) {

    companion object {
        const val JOB_NAME = "jobRunner"
    }

    @Bean
    fun job(step1: Step, step2: Step): Job {
        return JobBuilder("jobRunnerJob", jobRepository)
            .start(step1)
            .next(step2)
            .listener(JobResultListener())
            .build()
    }

    @Bean
    fun step1(transactionManager: PlatformTransactionManager): Step {
        return StepBuilder("step1", jobRepository)
            .tasklet({ stepContribution, _ ->
                val jobInstance: JobInstance = stepContribution.stepExecution.jobExecution.jobInstance
                println("jobInstance.getId() : " + jobInstance.id)
                println("jobInstance.getInstanceId() : " + jobInstance.instanceId)
                println("jobInstance.getJobName() : " + jobInstance.jobName)
                println("jobInstance.getJobVersion : " + jobInstance.version)

                println("contribution.getExitStatus(): " + stepContribution.exitStatus);
                println("contribution.getStepExecution().getStepName(): " + stepContribution.stepExecution.stepName);
//                stepContribution.exitStatus = ExitStatus.STOPPED;
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