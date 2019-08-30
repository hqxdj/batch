package com.ucasp.enter.batch;

import org.springframework.batch.item.file.mapping.DefaultLineMapper;

/**
 * @author xdj
 * @version V1.0
 * @ProjectName: platform
 * @Description: 自定义的文件行映射器，用于处理出错的行记录
 * @Date 2019/8/29 14:39
 * Copyright (c)   xdj
 */

public class MyDefaultLineMapper<T> extends DefaultLineMapper<T> {


    @Override
    public T mapLine(String line, int lineNumber) throws Exception {

        try {
            super.mapLine(line, lineNumber);
        }catch (Exception e){
            // 将出错记录记录到redis？
            System.out.println(line + lineNumber);
        }

        return super.mapLine(line, lineNumber);
    }

}
