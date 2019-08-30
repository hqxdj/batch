package com.ucasp.enter.controller;

import com.ucasp.enter.service.JobLaunchServiceImpl;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xdj
 * @version V1.0
 * @ProjectName: platform
 * @Description:
 * @Date 2019/8/29 17:22
 * Copyright (c)   xdj
 */
@RestController
@RequestMapping("job")
public class JobLaunchController {
    @Autowired
    private JobLaunchServiceImpl jobLaunchService;

    @PostMapping("/launch")
    public String launchJob() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        return jobLaunchService.launchJob();
    }
}
