package kr.co.yunhalee.study.springbatch.configuration.jobparameter

import com.yunhalee.springbatch.infrastructure.Constants
import com.yunhalee.springbatch.listener.JobResultListener
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
import java.util.Date


@Configuration
@ConditionalOnProperty(Constants.PROPERTY_JOB_NAME, havingValue = JobParameterConfiguration.JOB_NAME)
class JobParameterConfiguration(
    private val jobRepository: JobRepository
) {

    companion object {
        const val JOB_NAME = "jobParameter"
    }

    @Bean
    fun job(step1: Step, step2: Step): Job {
        return JobBuilder("jobParameterJob", jobRepository)
            .start(step1)
            .next(step2)
            .listener(JobResultListener())
            .build()
    }

    @Bean
    fun step1(transactionManager: PlatformTransactionManager): Step {
        return StepBuilder("step1", jobRepository)
            .tasklet({ contribution, chunkContext ->

                val jobParameters = contribution.stepExecution.jobParameters
                val name = jobParameters.getString("name")
                val seq = jobParameters.getLong("seq")!!
                val date: Date? = jobParameters.getDate("date")

                println("===========================")
                println("name:$name")
                println("seq: $seq")
                println("date: $date")
                println("===========================")

                val jobParameters2: Map<String, Any> = chunkContext.stepContext.jobParameters
                val name2 = jobParameters2["name"] as String
                val seq2 = jobParameters2["seq"] as Long
                println("===========================")
                println("name:$name2")
                println("seq: $seq2")
                println("===========================")
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

}