package com.yunhalee.springbatch.configuration.flowjob

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
@ConditionalOnProperty(Constants.PROPERTY_JOB_NAME, havingValue = FlowJobConfiguration.JOB_NAME)
class FlowJobConfiguration(
    private val jobRepository: JobRepository
) {

    companion object {
        const val JOB_NAME = "flowJob"
    }

//    @Bean
//    fun job1(step1: Step, step2: Step): Job {
//        return JobBuilder("withoutFlowJob", jobRepository)
//            .start(step1)
//            .next(step2)
//            .build()
//    }


    @Bean
    fun job2(step1: Step, step2: Step, step3: Step, flow: Flow): Job {
        return JobBuilder("withFlowJob", jobRepository)
            .start(flow)
            .next(step2)
            .start(step1)
            .on("COMPLETED").to(step2)
            .from(step1)
            .on("FAILED")
            .to(step3)
            .end()
            .build()
    }

    @Bean
    fun step1(transactionManager: PlatformTransactionManager): Step {
        return StepBuilder("step1", jobRepository)
            .tasklet({ _, _ ->
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
            .tasklet({ _, _ ->
                println(" ============================")
                println(" >> step2 has executed")
                println(" ============================")
                RepeatStatus.FINISHED
            }, transactionManager)
            .build()
    }

    @Bean
    fun flow(step3: Step, step4: Step): Flow {
        val flowBuilder: FlowBuilder<Flow> = FlowBuilder<Flow>("flow")
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