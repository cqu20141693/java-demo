package com.wujt.collections.list;

import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @author wujt
 *
 * Skip list的性质
 *
 * (1) 由很多层结构组成，level是通过一定的概率随机产生的。
 * (2) 每一层都是一个有序的链表，默认是升序
 * (3) 最底层(Level 1)的链表包含所有元素。
 * (4) 如果一个元素出现在Level i 的链表中，则它在Level i 之下的链表也都会出现。
 * (5) 每个节点包含多个指针，一个指向同一链表中的下一个元素，一个指向下面一层的元素。
 *
 * 原文链接：https://blog.csdn.net/bigtree_3721/article/details/51291974
 */
public class SkipListDemo {
    public static void main(String[] args) {
        //SkipList更像Java中的TreeMap。TreeMap基于红黑树（一种自平衡二叉查找树）实现的，时间复杂度平均能达到O(log n)
        ConcurrentSkipListMap<String, String> concurrentSkipListMap = new ConcurrentSkipListMap<>();
        // ConcurrentSkipListSet是通过ConcurrentSkipListMap实现的
        ConcurrentSkipListSet<String> objects = new ConcurrentSkipListSet<>();
    }
}
