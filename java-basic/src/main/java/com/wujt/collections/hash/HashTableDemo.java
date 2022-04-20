package com.wujt.collections.hash;

import java.util.Hashtable;

/**
 * 利用synchronized对方法进行加锁的线程安全的散列表
 * @author wujt
 */
public class HashTableDemo {
    public static void main(String[] args) {
        Hashtable<String, String> hashtable = new Hashtable<>();
        hashtable.put("key","value");
    }
}
