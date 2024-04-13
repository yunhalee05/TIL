package com.yunhalee.springbatch.configuration.async

import com.yunhalee.springbatch.infrastructure.Constants
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.support.SynchronizedItemReader
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.transaction.PlatformTransactionManager
import java.util.Locale


@Configuration
@ConditionalOnProperty(Constants.PROPERTY_JOB_NAME, havingValue = AsyncConfiguration.JOB_NAME)
class AsyncConfiguration(
    private val jobRepository: JobRepository
) {

    companion object {
        const val JOB_NAME = "async"
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
            .reader(SynchronizedItemReader(CustomItemReader(listOf("data1", "data2", "data3", "data4", "data5", "data6", "data7", "data8", "data9", "data10"))))
            // 동기화 이슈 확인
//            .reader(CustomItemReader(listOf("data1", "data2", "data3", "data4", "data5", "data6", "data7", "data8", "data9", "data10")))
            .processor { item -> item.uppercase(Locale.getDefault()) }
            .writer { list ->
                println("-------start chunk write--------")
                list.forEach { println(it) }
                println("-------finish chunk write--------")
            }
            .taskExecutor(ThreadPoolTaskExecutor().apply {
                maxPoolSize = 5
                maxPoolSize = 8
                setThreadNamePrefix("async-thread")
            })
//            .throttleLimit(4) // 5.0부터 deprecated 됨
            .build()
    }

    @Bean
    fun job2(step1: Step, step2: Step): Job {
        return JobBuilder("innerJob", jobRepository)
            .start(step1)
            .next(step2)
            .build()
    }

}