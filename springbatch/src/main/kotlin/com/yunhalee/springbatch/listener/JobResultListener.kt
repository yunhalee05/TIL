package com.yunhalee.springbatch.listener

import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobExecutionListener


class JobResultListener: JobExecutionListener {

    override fun beforeJob(jobExecution: JobExecution) {
        println(" ============================")
        println(" >> job has executed")
        println(" ============================")
    }

    override fun afterJob(jobExecution: JobExecution) {
        println("=================================== Job name : ${jobExecution.jobInstance.jobName} ===============================================")
        println("jobExecution.getJobInstance().getInstanceId() : " + jobExecution.jobInstance.instanceId)
        println("jobExecution.getStatus() : " + jobExecution.status)
        println("jobExecution.getExitStatus() : " + jobExecution.exitStatus)
        println("jobExecution.getCreateTime() : " + jobExecution.createTime)
        println("jobExecution.getStartTime() : " + jobExecution.startTime)
        println("jobExecution.getEndTime() : " + jobExecution.endTime)
        println("jobExecution.getExecutionContext : " + jobExecution.executionContext)
        println("jobExecution.getJobParameters().getParameters() : " + jobExecution.jobParameters.parameters)

        val stepExecutions = jobExecution.stepExecutions
        for (stepExecution in stepExecutions) {
            println("================================= Step name : ${stepExecution.stepName} =====================================================")
            println("stepExecution.getStatus() : " + stepExecution.status)
            println("stepExecution.getExitStatus() : " + stepExecution.exitStatus)
            println("stepExecution.getCommitCount() : " + stepExecution.commitCount)
            println("stepExecution.getRollbackCount() : " + stepExecution.rollbackCount)
            println("stepExecution.getReadCount() : " + stepExecution.readCount)
            println("stepExecution.getReadSkipCount() : " + stepExecution.readSkipCount)
            println("stepExecution.getStartTime() : " + stepExecution.startTime)
            println("stepExecution.getEndTime() : " + stepExecution.endTime)
            println("stepExecution.getWriteCount() : " + stepExecution.writeCount)
            println("stepExecution.getWriteSkipCount() : " + stepExecution.writeSkipCount)
            println("stepExecution.getFilterCount() : " + stepExecution.filterCount)
            println("stepExecution.getProcessSkipCount() : " + stepExecution.processSkipCount)
            println("stepExecution.getSkipCount() : " + stepExecution.skipCount)
            println("stepExecution.getExecutionContext() : " + stepExecution.executionContext)
        }
    }
}