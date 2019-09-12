package com.ucasp.enter.batch;

import com.ucasp.enter.entity.ChannelFile;
import com.ucasp.enter.util.SingletonConcurrentHashMapUtil;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

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
        ConcurrentHashMap<String, List<ChannelFile>> channelInstance = SingletonConcurrentHashMapUtil.getChannelInstance();
        for (ChannelFile channelFile : list) {
            if (channelInstance.get(channelFile.getCustomerNum()) == null) {
                ArrayList<ChannelFile> channelFiles = new ArrayList<>();
                channelFiles.add(channelFile);
                channelInstance.put(channelFile.getCustomerNum(), channelFiles);
            } else {
                List<ChannelFile> channelFiles = channelInstance.get(channelFile.getCustomerNum());
                channelFiles.add(channelFile);
                channelInstance.put(channelFile.getCustomerNum(), channelFiles);
            }
        }
    }
}
