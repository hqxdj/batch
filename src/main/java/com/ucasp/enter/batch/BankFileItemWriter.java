package com.ucasp.enter.batch;

import com.ucasp.enter.entity.BankFile;
import com.ucasp.enter.util.SingletonConcurrentHashMapUtil;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

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
        ConcurrentHashMap<String, List<BankFile>> bankInstance = SingletonConcurrentHashMapUtil.getBankInstance();
        for (BankFile bankFile : list) {
            if (bankInstance.get(bankFile.getCustomerNum()) == null) {
                ArrayList<BankFile> bankFiles = new ArrayList<>();
                bankFiles.add(bankFile);
                bankInstance.put(bankFile.getCustomerNum(), bankFiles);
            } else {
                List<BankFile> bankFiles = bankInstance.get(bankFile.getCustomerNum());
                bankFiles.add(bankFile);
                bankInstance.put(bankFile.getCustomerNum(), bankFiles);
            }
        }

    }
}
