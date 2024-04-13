package com.yunhalee.springbatch.configuration.partitioning

import com.yunhalee.springbatch.infrastructure.Constants
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.support.SynchronizedItemReader
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.SimpleAsyncTaskExecutor
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.transaction.PlatformTransactionManager
import java.util.Locale
import javax.sql.DataSource


@Configuration
@ConditionalOnProperty(Constants.PROPERTY_JOB_NAME, havingValue = PartitioningConfiguration.JOB_NAME)
class PartitioningConfiguration(
    private val jobRepository: JobRepository,
    private val dataSource: DataSource
) {

    companion object {
        const val JOB_NAME = "partitioning"
    }

    @Bean
    fun job1(step1: Step, slaveStep: Step, step3: Step, step4: Step, transactionManager: PlatformTransactionManager): Job {
        return JobBuilder("job", jobRepository)
            .incrementer(RunIdIncrementer())
            .start(masterStep(step1, slaveStep, transactionManager))
            .start(step1)
            .next(step3)
            .next(step4)
            .build()
    }


    @Bean
    fun masterStep(step1: Step, slaveStep: Step, transactionManager: PlatformTransactionManager): Step {
        return StepBuilder("masterStep", jobRepository)
            .partitioner(step1.name, partitioner())
            .step(slaveStep)
            .gridSize(4)
            .taskExecutor(SimpleAsyncTaskExecutor())
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
    fun partitioner(): ColumnRangePartitioner {
        val columnRangePartitioner = ColumnRangePartitioner()
        columnRangePartitioner.column = "id"
        columnRangePartitioner.setDataSource(this.dataSource)
        columnRangePartitioner.table = "customer"
        return columnRangePartitioner
    }

    @Bean
    fun slaveStep(transactionManager: PlatformTransactionManager): Step {
        return StepBuilder("slaveStep", jobRepository)
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
    fun job2(step1: Step, step3: Step): Job {
        return JobBuilder("innerJob", jobRepository)
            .start(step1)
            .next(step3)
            .build()
    }

}