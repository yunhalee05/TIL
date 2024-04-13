package com.yunhalee.springbatch.configuration.parallelstep

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
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.transaction.PlatformTransactionManager


@Configuration
@ConditionalOnProperty(Constants.PROPERTY_JOB_NAME, havingValue = ParallelStepConfiguration.JOB_NAME)
class ParallelStepConfiguration(
    private val jobRepository: JobRepository
) {

    companion object {
        const val JOB_NAME = "parallel-step"
    }

    @Bean
    fun job1(flow1:Flow, flow2:Flow): Job {
        return JobBuilder("job", jobRepository)
            .start(flow1)
            .split(ThreadPoolTaskExecutor().apply {
                maxPoolSize = 5
                maxPoolSize = 8
                setThreadNamePrefix("async-thread")
            })
            .add(flow2)
            .end()
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
    fun step2(transactionManager: PlatformTransactionManager): Step {
        return StepBuilder("step2", jobRepository)
            .tasklet({ contribution, chunkContext ->
                println(" ============================")
                println(" >> step2 has executed")
                println(" ============================")
//                chunkContext.stepContext.stepExecution.status = BatchStatus.FAILED
//                contribution.exitStatus = ExitStatus.STOPPED
                RepeatStatus.FINISHED
            }, transactionManager)
            .build()
    }

    @Bean
    fun flow1(step1: Step, step2: Step): Flow {
        val flowBuilder: FlowBuilder<Flow> = FlowBuilder<Flow>("flow1")
        flowBuilder.start(step1)
            .next(step2)
            .end()
        return flowBuilder.build()
    }




    @Bean
    fun flow2(step3: Step, step4: Step): Flow {
        val flowBuilder: FlowBuilder<Flow> = FlowBuilder<Flow>("flow2")
        flowBuilder.start(step3)
            .next(step4)
            .end()
        return flowBuilder.build()
    }


    @Bean
    fun step3(transactionManager: PlatformTransactionManager): Step {
        return StepBuilder("step3", jobRepository)
            .tasklet({ _, _ ->
                println(" ============================")
                println(" >> step3 has executed")
                println(" ============================")
                RepeatStatus.FINISHED
            }, transactionManager)
            .build()
    }

    @Bean
    fun step4(transactionManager: PlatformTransactionManager): Step {
        return StepBuilder("step4", jobRepository)
            .tasklet({ _, _ ->
                println(" ============================")
                println(" >> step4 has executed")
                println(" ============================")
                RepeatStatus.FINISHED
            }, transactionManager)
            .build()
    }

}