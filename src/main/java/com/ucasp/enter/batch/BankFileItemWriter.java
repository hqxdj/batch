package com.ucasp.enter.batch;

import com.ucasp.enter.entity.BankFile;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author xdj
 * @version V1.0
 * @ProjectName: ucasp-enter-parse
 * @Description: 文件输出器
 * @Date 2019/9/11 16:20
 * Copyright (c)   xdj
 */
@Component
public class BankFileItemWriter implements ItemWriter<BankFile> {

    @Override
    public void write(List<? extends BankFile> list) throws Exception {
        for (BankFile bankFile : list) {
            System.out.println(bankFile);
        }

    }
}
