package com.wujt.array;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author gow
 * @date 2021/9/8
 */
public class 两个数组的交集 {

    public int[] intersect(int[] nums1, int[] nums2) {
        // 利用hash表存储，空间换时间
        if (nums1 == null || nums1.length == 0) {
            return new int[0];
        }
        if (nums2 == null || nums2.length == 0) {
            return new int[0];
        }

        ArrayList<Integer> list = new ArrayList<>();
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>(nums1.length);
        for (int j : nums1) {
            map.merge(j, 1, Integer::sum);
        }

        for (int i : nums2) {
            Integer size = map.get(i);
            if (size != null && size > 0) {
                list.add(i);
                map.put(i, size - 1);
            }
        }

        int[] ints = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            ints[i] = list.get(i);
        }
        return ints;

    }
}
