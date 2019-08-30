package com.ucasp.enter.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author xdj
 * @version V1.0
 * @ProjectName: ucasp-enter-parse
 * @Description: 单元自测
 * @Date 2019/8/30 15:02
 * Copyright (c)   xdj
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisUtilTest {

    @Resource
    private RedisUtil redisUtil;

    @Test
    public void set() {
        redisUtil.set();
        System.out.println("11");
    }
}