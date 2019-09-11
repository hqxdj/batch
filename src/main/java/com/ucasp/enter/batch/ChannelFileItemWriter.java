package com.ucasp.enter.batch;

import com.ucasp.enter.entity.ChannelFile;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author xdj
 * @version V1.0
 * @ProjectName: platform
 * @Description: 文件输出器
 * @Date 2019/8/27 10:14
 * Copyright (c)   xdj
 */
@Component
public class ChannelFileItemWriter implements ItemWriter<ChannelFile> {
    @Override
    public void write(List<? extends ChannelFile> list) throws Exception {
        for (ChannelFile channelFile : list) {
            System.out.println(channelFile);
        }
        System.out.println(Thread.currentThread().getName() + "========================");
    }
}
