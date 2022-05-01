package com.wujt.collections.list;

import java.util.LinkedList;

/**
 * LinkedList 链表实现
 * <p>
 * fields
 * int size = 0
 * Node<E> first
 * Node<E> last
 * <p>
 * Node 是一个双向节点
 *
 * @author wujt
 */
public class LinkedListDemo {
    public static void main(String[] args) {
        LinkedList linkedList = new LinkedList();
        linkedList.add(1);
        linkedList.add(2);
        linkedList.add(3);
        linkedList.removeLast(); // 删除最后一个
        int size = linkedList.size();
        linkedList.add(3);
        System.out.println(linkedList);
        linkedList.pop(); // 删除第一个
        System.out.println(linkedList);
    }
}
