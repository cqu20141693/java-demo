package leetcode.editor.cn;
//给你一个整数数组 nums ，除某个元素仅出现 一次 外，其余每个元素都恰出现 三次 。请你找出并返回那个只出现了一次的元素。 
//
// 
//
// 示例 1： 
//
// 
//输入：nums = [2,2,3,2]
//输出：3
// 
//
// 示例 2： 
//
// 
//输入：nums = [0,1,0,1,0,1,99]
//输出：99
// 
//
// 
//
// 提示： 
//
// 
// 1 <= nums.length <= 3 * 10⁴ 
// -2³¹ <= nums[i] <= 2³¹ - 1 
// nums 中，除某个元素仅出现 一次 外，其余每个元素都恰出现 三次 
// 
//
// 
//
// 进阶：你的算法应该具有线性时间复杂度。 你可以不使用额外空间来实现吗？ 
// Related Topics 位运算 数组 👍 718 👎 0


import java.util.HashMap;
import java.util.Set;

//leetcode submit region begin(Prohibit modification and deletion)
class Solution {
    public int singleNumber(int[] nums) {
       // return useMap(nums);

        int data=0;

        for (int i = 0; i < 32; i++) {
            int count=0;
            for (int num : nums) {
                count+=((num>>i)&1);
            }
            if(count%3!=0){
                data|=(1<<i);
            }
        }
        return data;
    }

    private int useMap(int[] nums) {
        // 不考虑空间复杂度则遍历时存储已经出现的数字，并记录出现次数。
        // 二次遍历出现次数为1的返回；

        HashMap<Integer, Integer> map = new HashMap<>();

        for (int num : nums) {
            map.put(num, map.getOrDefault(num, 0) + 1);
        }

        Set<Integer> keySet = map.keySet();
        for (Integer key : keySet) {
            if (map.get(key) == 1) {
                return key;
            }
        }

        return -1;
    }
}
//leetcode submit region end(Prohibit modification and deletion)
