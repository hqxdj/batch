package com.ucasp.enter.batch;

import org.springframework.batch.core.StepListener;
import org.springframework.batch.core.listener.ItemListenerSupport;
import org.springframework.stereotype.Component;

/**
 * @author xdj
 * @version V1.0
 * @ProjectName: platform
 * @Description:
 * @Date 2019/8/27 17:39
 * Copyright (c)   xdj
 */

@Component
public class BeforeItemReaderFilter extends ItemListenerSupport implements StepListener {


    public void beforeRead() {

        System.out.println("读取前置空值");
    }

    @Override
    public void onReadError(Exception ex) {
        super.onReadError(ex);
    }
}
