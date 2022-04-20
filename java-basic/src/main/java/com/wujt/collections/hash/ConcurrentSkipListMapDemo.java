package com.wujt.collections.hash;

import java.util.concurrent.ConcurrentSkipListMap;

/**
 * ConcurrentSkipListMap： 线程安全的基于跳表的散列表实现
 *
 *
 * @author wujt
 */
public class ConcurrentSkipListMapDemo {
    public static void main(String[] args) {
        ConcurrentSkipListMap<String, String> skipListMap = new ConcurrentSkipListMap<>();
        skipListMap.put("name","taoge");
    }
}
