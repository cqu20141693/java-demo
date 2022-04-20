package com.wujt.collections.hash;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author wujt
 */
public class MyLRU<K, V> extends LinkedHashMap<K, V> {
    private Integer cacheSize;

    MyLRU(Integer size) {
        this.cacheSize = size;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> entry) {
        return size() > cacheSize;
    }
}
