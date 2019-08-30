package com.ucasp.enter.batch;

import com.ucasp.enter.entity.Customer;
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
public class FlatMapItemWriter implements ItemWriter<Customer> {
    @Override
    public void write(List<? extends Customer> list) throws Exception {
        for (Customer customer : list) {
            System.out.println(customer);
        }
        System.out.println(Thread.currentThread().getName() + "========================");
    }
}
