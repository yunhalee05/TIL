package com.yunhalee.springbatch.configuration.jobrepository

import com.yunhalee.springbatch.infrastructure.Constants
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobExecutionListener
import org.springframework.batch.core.repository.JobRepository
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(Constants.PROPERTY_JOB_NAME, havingValue = BatchConfigurationConfiguration.JOB_NAME)
class JobRepositoryListener(
    private val jobRepository: JobRepository
) : JobExecutionListener {

    override fun afterJob(jobExecution: JobExecution) {
        val jobName = jobExecution.jobInstance.jobName
        val jobParameters = jobExecution.jobParameters
        val lastExecution: JobExecution? = jobRepository.getLastJobExecution(jobName, jobParameters)
        lastExecution?.let {
            for (execution in lastExecution.stepExecutions) {
                val status = execution.status
                println("BatchIsRunning = " + status.isRunning)
                println("BatchStatus = " + status.name)
            }
        }
    }
}