package com.wujt.collections.queue;

import java.util.ArrayDeque;
import java.util.LinkedList;

/**
 * 双端队列：
 * 新增API:
 * 提供了add/offer/remove/poll,peek First/Last() 方法
 *
 * @author wujt
 */
public class DequeDemo {
    public static void main(String[] args) {
        // 链式双端队列
        LinkedList<Object> linkedList = new LinkedList<>();
        //顺序双端队列
        ArrayDeque<Object> arrayDeque = new ArrayDeque<>();
    }
}
