package com.wujt.collections.hash;

import java.util.concurrent.ConcurrentHashMap;

/**
 * ConcurrentHashMap 1.7和1.8的区别
 * 1、整体结构
 * 1.7：Segment + HashEntry + Unsafe
 * 1.8: 移除Segment，使锁的粒度更小，Synchronized + CAS + Node + Unsafe
 * <p>
 * 2、put（）
 * 1.7：先定位Segment，再定位桶，put全程加锁，没有获取锁的线程提前找桶的位置，并最多自旋64次获取锁，超过则挂起。
 * <p>
 * 1.8：由于移除了Segment，类似HashMap，可以直接定位到桶，拿到first节点后进行判断，1、为空则CAS插入；2、为-1则说明在扩容，则跟着一起扩容；3、else则加锁put（类似1.7）
 * <p>
 * 3、get（）
 * 基本类似，由于value声明为volatile，保证了修改的可见性，因此不需要加锁。（数组弄了Volatile，同时对数组元素的修改也利用的Unsafe 实现了可见性更新）
 * <p>
 * 1.8：支持并发扩容，HashMap扩容在1.8中由头插改为尾插（为了避免死循环问题），ConcurrentHashMap也是，迁移也是从尾部开始，
 * 每个线程扩容前,先判断当前桶是否被扩容处理了；然后先获取当前桶的锁，然后在oldTable 桶的头部放置一个hash值为-1的节点；这样别的线程访问时就能判断是否该桶已经被其他线程处理过了。然后开始扩容操作；和HashMap的扩容一致
 * <p>
 *
 * 问题
 * <p>
 * 如何在很短的时间内将大量数据插入到ConcurrentHashMap？
 * 主要的消耗：第一个是扩容操作，第二个是锁资源的争夺。
 * 第一个可以预先设置一个合适的容量，避免扩容
 * 2. 我们可以对HashCode 进行预处理；将会出现在一个桶的放到一个线程中插入；不同桶的可以用不同的线程；这里注意线程资源的控制-线程池。
 * <p>
 * ConcurrentHashMap的get方法是否要加锁，为什么？
 * get方法就是从Hash表中读取数据，并不会与扩容不冲突，因此该方法也不需要同步锁
 * <p>
 * 为什么使用ConcurrentHashMap
 * 线程安全，HashMap在多线程中进行put方法有可能导致程序死循环，因为多线程可能会导致HashMap形成环形链表
 * 高性能，利用cas和volatile（table[]，key,value）减少锁的使用，并利用分段锁思想，只对需要操作的桶进行Synchronized锁,利用红黑树在冲突比较明显的情况下增加查询能力
 *
 * HashMap的数据结构？ 数组+ 链表+ 红黑树 ，
 *
 * HashMap什么时候扩容？ 扩容量是多大？
 * 扩容因子是0.75，当size>sizeCtl时进行扩容，扩容直接扩容一倍
 *
 * HashMap什么时候从链表转红黑树？ 当节点size>=8 时，什么时候转为链表，少于6个时
 *
 *
 * 源码解析： https://developer.aliyun.com/article/673765
 *
 * @author wujt
 */
public class ConcurrentHashMapDmeo {
    public static void main(String[] args) {
        ConcurrentHashMap<String, String> concurrentHashMap = new ConcurrentHashMap<>();
        concurrentHashMap.put("key", "value");
        String key = concurrentHashMap.get("key");


        String orDefault = concurrentHashMap.getOrDefault("name", "gow");
        String name = concurrentHashMap.get("name");
        String ifAbsent = concurrentHashMap.computeIfAbsent("name", (k) -> "gow");
        String name1 = concurrentHashMap.get("name");
        System.out.println(orDefault + ":" + name + ":" + ifAbsent + ":" + name1);
        // concurrencyLevel=1
        ConcurrentHashMap<String, String> initMap1 = new ConcurrentHashMap<>(1024, 0.75f);
        // default concurrencyLevel=16
        ConcurrentHashMap<String, String> initMap = new ConcurrentHashMap<>(1024, 0.75f, 16);

    }
}
