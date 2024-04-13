package kr.co.yunhalee.study.springbatch.configuration.jobparameter

import com.yunhalee.springbatch.infrastructure.Constants
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import java.util.Date

@Component
@ConditionalOnProperty(Constants.PROPERTY_JOB_NAME, havingValue = JobParameterConfiguration.JOB_NAME)
class JobRunner(
    private val jobLauncher: JobLauncher,
    private val job: Job
) : ApplicationRunner{

    override fun run(args: ApplicationArguments?) {
        val parameters = JobParametersBuilder()
            .addString("name", "testUser")
            .addLong("seq", 1L)
            .addDate("date", Date())
            .toJobParameters()
        jobLauncher.run(job, parameters)
    }
}