package com.wujt.collections;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author wujt
 */
public class CollectionsDemo {
    public static void main(String[] args) {
        // 测试集合排序
        testArraySort();
        int[] ints = {1, 3, 4, 5};
        int[] ofRange = Arrays.copyOfRange(ints, 1, 3);
        System.out.println("success");

    }

    /**
     * Collections.sort方法或者是Arrays.sort方法底层实现都是TimSort实现的,jdk1.7新增的，以前是归并排序
     * TimSort算法是一种起源于归并排序和插入排序的混合排序算法,原则上TimSort是归并排序，但小片段的合并中用了插入排序
     */
    private static void testArraySort() {
        List<String> strings = Arrays.asList("6", "1", "3", "1", "2");
        //底层调用的  Arrays.sort(a, c);
        Collections.sort(strings);

        for (String string : strings) {

            System.out.println(string);
        }
    }
}
