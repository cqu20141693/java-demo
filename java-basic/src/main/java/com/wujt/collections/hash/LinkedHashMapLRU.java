package com.wujt.collections.hash;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author wujt
 */
public class LinkedHashMapLRU<K, V> extends LinkedHashMap<K, V> {
    private int cacheSize;

    public LinkedHashMapLRU(int cacheSize) {
        super(16, 0.75f, true);
        this.cacheSize = cacheSize;
    }

    /**
     * 判断元素个数是否超过缓存容量，移除最久没有使用的元素，即链表尾部
     */
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > cacheSize;
    }
}
