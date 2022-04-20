package com.wujt.java10;

import java.util.ArrayList;
import java.util.List;

/**
 * 用 of 和 copyOf 创建的集合为不可变集合，不能进行添加、删除、替换、排序等操作，
 * 不然会报 java.lang.UnsupportedOperationException 异常
 *
 * @author gow 2021/06/13
 */
public class CollectionDemo {
    public static void main(String[] args) {
        var list = List.of("Java", "Python", "C");
        // copy 不可变的集合
        var copy = List.copyOf(list);
        System.out.println(list == copy);   // true

        list = new ArrayList<String>();
        copy = List.copyOf(list);
        System.out.println(list == copy);   // false
    }
}
