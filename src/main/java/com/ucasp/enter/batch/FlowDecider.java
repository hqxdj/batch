package com.ucasp.enter.batch;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @author xdj
 * @version V1.0
 * @ProjectName: platform
 * @Description: 步骤流程决策器
 * @Date 2019/8/29 10:02
 * Copyright (c)   xdj
 */

@Component
public class FlowDecider implements JobExecutionDecider {
    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        Collection<StepExecution> stepExecutions = jobExecution.getStepExecutions();
        for (StepExecution s : stepExecutions) {
            System.out.println(s.getStatus());
        }
        return FlowExecutionStatus.COMPLETED;
    }
}
