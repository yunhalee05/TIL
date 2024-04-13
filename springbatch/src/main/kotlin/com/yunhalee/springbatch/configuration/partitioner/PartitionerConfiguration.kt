package com.yunhalee.springbatch.configuration.partitioner

import com.yunhalee.springbatch.infrastructure.Constants
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.FlowBuilder
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.job.flow.Flow
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager


@Configuration
@ConditionalOnProperty(Constants.PROPERTY_JOB_NAME, havingValue = PartitionerConfiguration.JOB_NAME)
class PartitionerConfiguration(
    private val jobRepository: JobRepository
) {

    companion object {
        const val JOB_NAME = "partitioner"
    }

    @Bean
    fun job1(step1: Step, step2: Step, step3: Step, step4: Step): Job {
        return JobBuilder("job", jobRepository)
            .start(step1)
            .next(step2)
            .next(step3)
            .next(step4)
            .build()
    }


    @Bean
    fun step1(transactionManager: PlatformTransactionManager): Step {
        return StepBuilder("step1", jobRepository)
            .tasklet({ contribution, chunkContext ->
                println(" ============================")
                println(" >> step1 has executed")
                println(" ============================")
//                chunkContext.stepContext.stepExecution.status = BatchStatus.FAILED
//                contribution.exitStatus = ExitStatus.STOPPED
                RepeatStatus.FINISHED
            }, transactionManager)
            .build()
    }

    @Bean
    fun step2(transactionManager: PlatformTransactionManager, step1: Step): Step {
        return StepBuilder("step2", jobRepository)
            .partitioner(step1)
            .gridSize(2)
            .build()
    }

    @Bean
    fun job2(step1: Step, step2: Step): Job {
        return JobBuilder("innerJob", jobRepository)
            .start(step1)
            .next(step2)
            .build()
    }


    @Bean
    fun step3(transactionManager: PlatformTransactionManager, job2:Job): Step {
        return StepBuilder("step3", jobRepository)
            .job(job2)
            .build()
    }

    @Bean
    fun step4(transactionManager: PlatformTransactionManager, flow: Flow): Step {
        return StepBuilder("step4", jobRepository)
            .flow(flow)
            .build()
    }

    @Bean
    fun flow(transactionManager: PlatformTransactionManager, step2: Step): Flow {
        val flowBuilder: FlowBuilder<Flow> = FlowBuilder<Flow>("flow")
        flowBuilder.start(step2)
            .end()
        return flowBuilder.build()
    }


}