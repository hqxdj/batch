package com.ucasp.enter.service;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xdj
 * @version V1.0
 * @ProjectName: platform
 * @Description:
 * @Date 2019/8/29 17:23
 * Copyright (c)   xdj
 */
@Service
public class JobLaunchServiceImpl {
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job job;

    public String launchJob() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        JobParameters parameters = new JobParametersBuilder()
                .addString("firstJob","e")
                .toJobParameters();
        JobExecution run = jobLauncher.run(job, parameters);
        return run.getExitStatus().getExitCode();
    }
}
