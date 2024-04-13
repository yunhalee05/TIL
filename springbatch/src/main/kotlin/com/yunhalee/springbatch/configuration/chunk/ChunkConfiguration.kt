package com.yunhalee.springbatch.configuration.chunk

import com.yunhalee.springbatch.infrastructure.Constants
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.FlowBuilder
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.job.flow.Flow
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.core.step.skip.LimitCheckingItemSkipPolicy
import org.springframework.batch.item.support.ListItemReader
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.batch.repeat.exception.SimpleLimitExceptionHandler
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.retry.backoff.FixedBackOffPolicy
import org.springframework.retry.policy.SimpleRetryPolicy
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import java.util.Locale


@Configuration
@ConditionalOnProperty(Constants.PROPERTY_JOB_NAME, havingValue = ChunkConfiguration.JOB_NAME)
class ChunkConfiguration(
    private val jobRepository: JobRepository
) {

    companion object {
        const val JOB_NAME = "simpleChunkWithFlow"
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
    fun step2(transactionManager: PlatformTransactionManager): Step {
        return StepBuilder("step2", jobRepository)
            .chunk<String, String>(3, transactionManager)
            .reader(ListItemReader(listOf("data1", "data2", "data3", "data4", "data5", "data6", "data7", "data8", "data9", "data10")))
            .processor { item -> item.uppercase(Locale.getDefault()) }
            .writer { list ->
                println("-------start chunk write--------")
                list.forEach{ println(it) }
                println("-------finish chunk write--------")
            }
            .faultTolerant()
            .noSkip(MethodArgumentTypeMismatchException::class.java)
            .skip(IllegalArgumentException::class.java)
            .skipLimit(1)
            .skipPolicy(LimitCheckingItemSkipPolicy(3, hashMapOf(Pair(IllegalArgumentException::class.java, true))))
            .noRollback(IllegalArgumentException::class.java)
            .retry(IllegalStateException::class.java)
            .retryLimit(2)
            .retryPolicy(SimpleRetryPolicy(2, mutableMapOf(Pair(java.lang.IllegalStateException::class.java, true))))
            .backOffPolicy(FixedBackOffPolicy().apply {
                backOffPeriod = 2000
            })
            .exceptionHandler(SimpleLimitExceptionHandler(3))
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