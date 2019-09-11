package com.ucasp.enter.batch;

import com.ucasp.enter.entity.ChannelFile;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * @author xdj
 * @version V1.0
 * @ProjectName: platform
 * @Description:
 * @Date 2019/8/28 15:49
 * Copyright (c)   xdj
 */
@Component
public class ChannelFileProcessor implements ItemProcessor<ChannelFile, ChannelFile> {

    @Override
    public ChannelFile process(ChannelFile channelFile) throws Exception {
        return channelFile;
    }
}
