package com.ucasp.enter.util;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author xdj
 * @version V1.0
 * @ProjectName: ucasp-enter-parse
 * @Description:
 * @Date 2019/9/10 14:07
 * Copyright (c)   xdj
 */

public class Test {


    @org.junit.Test
    public void test() {
//        String s = "111.txt";
//        String[] split = s.split("\\.");
//        System.out.println(Arrays.asList(split));

        String[] s = {"a", "b", "c", "d", "a"};
//        for (int i = 0; i < s.length; i++) {
//            for (int j = i+1; j < s.length; j++) {
//                System.out.println(s[i] == s[j]);
//            }
//        }
        List<String> collect = Arrays.asList(s).stream()   // list 对应的 Stream
                .collect(Collectors.toMap(e -> e, e -> 1, Integer::sum))
                // 获得元素出现频率的 Map，键为元素，值为元素出现的次数
                .entrySet().stream()
                // 所有 entry 对应的 Stream
                .filter(entry -> entry.getValue() == 1)
                // 过滤出元素出现次数大于 1 的 entry
                .map(Map.Entry::getKey)
                // 获得 entry 的键（重复元素）对应的 Stream
                .collect(Collectors.toList());
        System.out.println(collect);


    }


}
