package com.ucasp.enter.batch;

import com.ucasp.enter.entity.BankFile;
import com.ucasp.enter.entity.ChannelFile;
import com.ucasp.enter.util.SingletonConcurrentHashMapUtil;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

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

        System.out.println("勾兑开始-----------------------------------------");
        ConcurrentHashMap<String, List<ChannelFile>> channelInstance = SingletonConcurrentHashMapUtil.getChannelInstance();
        System.out.println("当前渠道文件中有多少个商户----->" + channelInstance.keySet().size());

        ConcurrentHashMap<String, List<BankFile>> bankInstance = SingletonConcurrentHashMapUtil.getBankInstance();
        System.out.println("当前银行文件中有多少个商户----->" + bankInstance.keySet().size());

        System.out.println("勾兑完成-----------------------------------------");
        return RepeatStatus.FINISHED;
    }
}
