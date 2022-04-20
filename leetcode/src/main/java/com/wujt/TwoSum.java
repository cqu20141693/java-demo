package com.wujt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author wujt
 */
public class TwoSum {

    public static void main(String[] args) {

    }

    /**
     * 使用哈希表，可以将寻找 target - x 的时间复杂度降低到从 O(N)O(N) 降低到 O(1)O(1)。
     * 这样我们创建一个哈希表，对于每一个 x，我们首先查询哈希表中是否存在 target - x，然后将 x 插入到哈希表中，即可保证不会让 x 和自己匹配
     *
     * @param nums
     * @param target
     * @return
     */
    public int[] twoSum(int[] nums, int target) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(target - nums[i])) {
                return new int[]{i, map.get(target - nums[i])};
            } else {
                map.put(nums[i], i);
            }

        }
        return new int[0];
    }

    public int[] twoSum2(int[] nums, int target) {
        //思路 准备使用空间换取时间
        //准备进行一次数据排序，1. 在排序过程中对每个数字对应的下标进行存储；2. 同时找到比目标数字小中最大的数字（目标是为了让我们循环查找的数据进行限制）；
        //然后通过二分法进行处理
        HashMap<Integer, ArrayList<Integer>> map = new HashMap(nums.length);
        for (int i = 0; i < nums.length; i++) {
            ArrayList<Integer> set;
            if (map.get(nums[i]) == null) {
                set = new ArrayList<>();

            } else {
                set = map.get(nums[i]);
            }
            set.add(i);
            map.put(nums[i], set);
        }
        //开始寻找
        for (int i = 0; i < nums.length; i++) {
            Integer tmp = target - nums[i];
            if (map.get(tmp) == null) {
                continue;
            }
            if (nums[i] == tmp) {
                if (map.get(tmp).size() == 1) {
                    continue;
                }
                Iterator<Integer> integerIterator = map.get(tmp).iterator();
                while (integerIterator.hasNext()) {
                    if (i == integerIterator.next()) {
                        integerIterator.remove();
                    }
                }
                return new int[]{i, map.get(tmp).get(0)};
            }

            return new int[]{i, map.get(tmp).get(0)};
        }
        return null;
    }
}
