package com.wujt.collections.hash;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * HashMap： 利用数组+ 链表+ 红黑树实现的散列表
 * <p>
 * 其散列函数： 对象实现的hash 方法后再取数据的高16位与低16位异或：
 * return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
 * <p>
 * 解决冲突的方式：在节点数少于8个时用链表；大于8个后使用红黑树,小于6个节点时退回链表
 * static final int TREEIFY_THRESHOLD = 8;
 * static final int UNTREEIFY_THRESHOLD = 6;
 *
 * 扩容因子： 默认0.75 ： 当散列表的元素size超过容量的0.75时开始扩容，扩容一倍
 * static final float DEFAULT_LOAD_FACTOR = 0.75f;
 * <p>
 * 为什么容量必须是2的幂：在计算桶的时候可以直接利用与运算替代取模hash
 * tab[i = (n - 1) & hash]
 * <p>
 * 扩容实现
 * java7： 通过直接将每个桶的链表全部遍历重新计算hash 值到新的桶空间中；
 * <p>
 * 1.8 ： 首先分桶进行处理；将桶中的元素的hash值和扩容前容量求与判断当前元素入桶的位置是老位置还是老位置移动扩容前容量为
 * if ((e.hash & oldCap) == 0)： 如果为真；表示hash值的最小值肯定可以加上oldCap ；所以需要移动到新的下一个桶。
 *
 * @description :
 */
public class HashMapDemo {
    public static void main(String[] args) {

        HashMap<String, Integer> hashMap = new HashMap<>();
        Map<String, String> map = new HashMap() {
            {
                put("test", "test");
            }
        };
        map.get("test");

        Set<Map.Entry<String, String>> entries = map.entrySet();

        testComputeIfAbsent();
    }

    private static void testComputeIfAbsent() {
        HashMap<String, Set<String>> map = new HashMap<>();
        String key = "test";
        Set<String> set = map.computeIfAbsent(key, t -> new HashSet<>());
        set.add("gow");
        Set<String> test = map.get(key);
        Set<String> ifAbsent = map.computeIfAbsent(key, t -> new HashSet<>());
        System.out.println(set.size()+":"+test.size()+":"+ifAbsent.size());
    }
}
