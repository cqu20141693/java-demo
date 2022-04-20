package leetcode.editor.cn;
//给你一个包含 n 个整数的数组 nums，判断 nums 中是否存在三个元素 a，b，c ，使得 a + b + c = 0 ？请你找出所有和为 0 且不重
//复的三元组。 
//
// 注意：答案中不可以包含重复的三元组。 
//
// 
//
// 示例 1： 
//
// 
//输入：nums = [-1,0,1,2,-1,-4]
//输出：[[-1,-1,2],[-1,0,1]]
// 
//
// 示例 2： 
//
// 
//输入：nums = []
//输出：[]
// 
//
// 示例 3： 
//
// 
//输入：nums = [0]
//输出：[]
// 
//
// 
//
// 提示： 
//
// 
// 0 <= nums.length <= 3000 
// -10⁵ <= nums[i] <= 10⁵ 
// 
// Related Topics 数组 双指针 排序 👍 3748 👎 0


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//leetcode submit region begin(Prohibit modification and deletion)
class Solution {
    public List<List<Integer>> threeSum(int[] nums) {
        // 三数之和实质时两数之和的升级版本：
        // 数组排序，保证递增，有利于进行优化判断和迭代


        ArrayList<List<Integer>> list = new ArrayList<>();
        if (nums == null || nums.length < 3) {
            return list;
        }
        int len = nums.length;

        // 排序保证后续的算法
        Arrays.sort(nums);
        for (int first = 0; first < len; first++) {
            // first 不能大于0
            if (nums[first] <= 0) {
                // 有序的，直接过滤掉后续一致的数据
                if (first > 0 && nums[first] == nums[first - 1]) {
                    continue;
                }
                int third = len - 1;
                // 下面的查找类似两数之和
                int target = -nums[first];
                for (int second = first + 1; second < len; second++) {
                    // 有序的，直接过滤掉后续一致的数据
                    if (second > first + 1 && nums[second] == nums[second - 1]) {
                        continue;
                    }
                    // 循环匹配second -> [second->n-1] 直到和不大于target
                    while (second < third && nums[second] + nums[third] > target) {
                        third--;
                    }
                    //
                    if (second == third) {
                        break;
                    }
                    if (nums[second] + nums[third] == target) {
                        ArrayList<Integer> integers = new ArrayList<>();
                        integers.add(nums[first]);
                        integers.add(nums[second]);
                        integers.add(nums[third]);
                        list.add(integers);
                    }
                }
            } else {
                break;
            }

        }
        return list;
    }
}
//leetcode submit region end(Prohibit modification and deletion)
