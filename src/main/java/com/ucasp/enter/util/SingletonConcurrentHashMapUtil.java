package com.ucasp.enter.util;

import com.ucasp.enter.entity.BankFile;
import com.ucasp.enter.entity.ChannelFile;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xdj
 * @version V1.0
 * @ProjectName: ucasp-enter-parse
 * @Description: 获取单例的集合实例
 * @Date 2019/9/12 14:37
 * Copyright (c)   xdj
 */

public class SingletonConcurrentHashMapUtil {

    private SingletonConcurrentHashMapUtil() {
    }

    public static ConcurrentHashMap<String, List<ChannelFile>> getChannelInstance() {
        return NewHashMapInstanceOfChannel.INSTANCE;
    }

    public static ConcurrentHashMap<String, List<BankFile>> getBankInstance() {
        return NewHashMapInstanceOfBank.INSTANCE;
    }

    static class NewHashMapInstanceOfChannel {
        static ConcurrentHashMap<String, List<ChannelFile>> INSTANCE = new ConcurrentHashMap<>();
    }

    static class NewHashMapInstanceOfBank {
        static ConcurrentHashMap<String, List<BankFile>> INSTANCE = new ConcurrentHashMap<>();
    }

}
