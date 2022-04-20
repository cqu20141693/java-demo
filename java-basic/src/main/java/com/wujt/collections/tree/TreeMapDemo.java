package com.wujt.collections.tree;

import java.util.TreeMap;

/**
 * 红黑树实现一种平衡排序树
 * @author wujt
 */
public class TreeMapDemo {
    public static void main(String[] args) {
        TreeMap<Integer, String> treeMap = new TreeMap<>();
        treeMap.put(1,"1");
        treeMap.put(3,"3");
        treeMap.put(2,"2");
        // 按照书序输出
        treeMap.forEach((k,v)->{
            System.out.println(String.format("key=%s,value=%s",k,v));
        });
        // 二叉查找算法进行查找
        String s = treeMap.get(3);
    }
}
