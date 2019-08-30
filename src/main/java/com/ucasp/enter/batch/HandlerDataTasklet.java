package com.ucasp.enter.batch;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

/**
 * @author xdj
 * @version V1.0
 * @ProjectName: platform
 * @Description: 勾兑的任务
 * @Date 2019/8/29 11:27
 * Copyright (c)   xdj
 */

@Component
public class HandlerDataTasklet implements Tasklet {


    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {


        System.out.println("从redis获取数据后，勾兑");
        return RepeatStatus.FINISHED;
    }
}
