package com.yunhalee.springbatch.configuration.joblauncher

import com.yunhalee.springbatch.infrastructure.Constants
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.util.Date


@RestController
@ConditionalOnProperty(Constants.PROPERTY_JOB_NAME, havingValue = JobLauncherConfiguration.JOB_NAME)
class JobLauncherController(
    private val jobLauncher: JobLauncher,
    private val job: Job
) {

    @PostMapping("/job-launcher-start")
    fun test() {
        val jobParameters = JobParametersBuilder()
            .addString("user", "test-user")
            .addDate("date", Date())
            .toJobParameters()
        jobLauncher.run(job, jobParameters)
    }
}