package com.wujt.collections.hash;

import java.util.LinkedHashMap;

/**
 * 实现原理：
 * 通过在数据写入table 数据表后；然后在通过一个回调函数维护添加的一个添加顺序的双向链表；
 * afterNodeAccess（）
 * <p>
 * 在entry 输出或者是foreach 时按照添加的顺序进行输出
 * <p>
 * <p>
 * static class Entry<K,V> extends HashMap.Node<K,V> {
 * Entry<K,V> before, after;
 * Entry(int hash, K key, V value, Node<K,V> next) {
 * super(hash, key, value, next);
 * }
 * <p>
 * // 双向链表头节点
 * transient LinkedHashMap.Entry<K,V> head;
 * <p>
 * // 双向链表尾节点
 * transient LinkedHashMap.Entry<K,V> tail;
 * <p>
 * // 指定遍历LinkedHashMap的顺序,true表示按照访问顺序,false表示按照插入顺序，默认为false
 * final boolean accessOrder;
 * }
 * <p>
 * field:
 * accessOrder : 获取的顺序 默认false ： 按照先进先出
 * API：
 * public LinkedHashMap(int initialCapacity,float loadFactor,boolean accessOrder) ：
 * <p>
 * 重写方法：
 * afterNodeInsertion(boolean evict)  ： 当添加节点的时候会判断是否需要删除old节点（扩展点）：总是返回false
 * void afterNodeAccess(Node<K,V> e) : 当节点被访问后；会将节点放到链表尾部；
 * afterNodeRemoval(Node<K,V> e)： 节点删除后；对应的对双向链表进行维护
 *
 * @author wujt
 */
public class LinkedHashMapDemo {
    public static void main(String[] args) {
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("name", "taog");
    }
}
