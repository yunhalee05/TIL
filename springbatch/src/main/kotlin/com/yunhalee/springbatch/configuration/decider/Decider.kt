package com.yunhalee.springbatch.configuration.decider

import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.StepExecution
import org.springframework.batch.core.job.flow.FlowExecutionStatus
import org.springframework.batch.core.job.flow.JobExecutionDecider
import org.springframework.stereotype.Component

@Component
class Decider : JobExecutionDecider {

    private var count = 0;
    override fun decide(jobExecution: JobExecution, stepExecution: StepExecution?): FlowExecutionStatus {
        count++;

        if (count % 2 == 0) {
            return FlowExecutionStatus("EVEN");
        } else {
            return FlowExecutionStatus("ODD");
        }
    }
}