package com.ucasp.enter.batch;

import com.ucasp.enter.entity.Customer;
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
public class BusinessProcessor implements ItemProcessor<Customer, Customer> {

    @Override
    public Customer process(Customer customer) throws Exception {
        System.out.println(Thread.currentThread().getName()+"++++++++++++++");
        return customer;
    }
}
