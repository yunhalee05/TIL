package com.yunhalee.springbatch.configuration.jobbuilderoption

import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.JobParametersIncrementer
import java.text.SimpleDateFormat
import java.util.Date

class JobIncrementer : JobParametersIncrementer {

    private val format = SimpleDateFormat("yyyy-MM-dd")
    override fun getNext(parameters: JobParameters?): JobParameters {
        val id = format.format(Date())
        return JobParametersBuilder().addString("run.id", id).toJobParameters()
    }
}