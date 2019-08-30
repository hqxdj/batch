package com.ucasp.enter.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author xdj
 * @version V1.0
 * @ProjectName: ucasp-enter-parse
 * @Description: redis工具类
 * @Date 2019/8/30 14:52
 * Copyright (c)   xdj
 */

@Component
public class RedisUtil {

    @Autowired
    private  RedisTemplate<String, String> redisTemplate;

    public  void set(){
        redisTemplate.opsForValue().set("test","test");
    }
}
