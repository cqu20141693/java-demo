package com.wujt.collections.list;

import java.util.ArrayList;

/**
 * ArrayList 数组实现，非线程安全
 * <p>
 * fields
 * int size: 当前集合的元素个数
 * Object[] elementData
 * 默认容量为10, 当没有指定容量时；默认elementData为空对象，首次add 时就会自动的扩容；扩容到默认容量
 * <p>
 * <p>
 *
 * 当size=length 时需要进行扩容
 *
 * 通用扩容策略：0.5
 * int newCapacity = oldCapacity + (oldCapacity >> 1);
 *
 * 扩容过程使用的Arrays.copy进行数据复制
 *
 * @author wujt
 */
public class ArrayListDemo {
    public static void main(String[] args) {

        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(1);
        arrayList.add(10);
        Integer[] objects = arrayList.toArray(new Integer[0]);
        for (Integer object : objects) {
            System.out.println(object);
        }
    }
}
