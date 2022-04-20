package leetcode.editor.cn;
//给定一个排序数组和一个目标值，在数组中找到目标值，并返回其索引。如果目标值不存在于数组中，返回它将会被按顺序插入的位置。 
//
// 请必须使用时间复杂度为 O(log n) 的算法。 
//
// 
//
// 示例 1: 
//
// 
//输入: nums = [1,3,5,6], target = 5
//输出: 2
// 
//
// 示例 2: 
//
// 
//输入: nums = [1,3,5,6], target = 2
//输出: 1
// 
//
// 示例 3: 
//
// 
//输入: nums = [1,3,5,6], target = 7
//输出: 4
// 
//
// 示例 4: 
//
// 
//输入: nums = [1,3,5,6], target = 0
//输出: 0
// 
//
// 示例 5: 
//
// 
//输入: nums = [1], target = 0
//输出: 0
// 
//
// 
//
// 提示: 
//
// 
// 1 <= nums.length <= 10⁴ 
// -10⁴ <= nums[i] <= 10⁴ 
// nums 为无重复元素的升序排列数组 
// -10⁴ <= target <= 10⁴ 
// 
// Related Topics 数组 二分查找 👍 1066 👎 0


//leetcode submit region begin(Prohibit modification and deletion)
class Solution {
    public int searchInsert(int[] nums, int target) {

        // 前置check
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int length = nums.length;
        if (target < nums[0] || target == nums[0]) {
            return 0;
        }
        if (target > nums[length - 1]) {
            return length;
        }
        if (target == nums[length - 1]) {
            return length - 1;
        }

        // 二分查找, 查找到退出，left==right(没有查找到)
        // 当找不到时，返回插入位置，应为当前right+1
        int left = 0;
        int right = length - 1;

        while (left <= right) {
            int middle = (left + right) >> 1;
            if (target == nums[middle]) {
                return middle;
            } else if (target < nums[middle]) {
                right = middle - 1;
            } else {
                left = middle + 1;
            }
        }
        return right + 1;
    }
}
//leetcode submit region end(Prohibit modification and deletion)
