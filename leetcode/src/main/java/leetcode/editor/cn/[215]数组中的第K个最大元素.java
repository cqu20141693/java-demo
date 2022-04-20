package leetcode.editor.cn;
//给定整数数组 nums 和整数 k，请返回数组中第 k 个最大的元素。 
//
// 请注意，你需要找的是数组排序后的第 k 个最大的元素，而不是第 k 个不同的元素。 
//
// 
//
// 示例 1: 
//
// 
//输入: [3,2,1,5,6,4] 和 k = 2
//输出: 5
// 
//
// 示例 2: 
//
// 
//输入: [3,2,3,1,2,4,5,5,6] 和 k = 4
//输出: 4 
//
// 
//
// 提示： 
//
// 
// 1 <= k <= nums.length <= 10⁴ 
// -10⁴ <= nums[i] <= 10⁴ 
// 
// Related Topics 数组 分治 快速选择 排序 堆（优先队列） 👍 1276 👎 0


//leetcode submit region begin(Prohibit modification and deletion)
class Solution {
    public int findKthLargest(int[] nums, int k) {
         // 利用快排思想，进行分治处理，从大到小排序
        return quickSortSearch(nums, 0, nums.length - 1, k-1);

    }

    private int quickSortSearch(int[] nums, int left, int right, int k) {
        if (right == left) {
            return nums[right];
        }
        int partition = partition(nums, left, right);
        if (partition == k) {
            return nums[partition];
        } else if (partition > k) {
            return quickSortSearch(nums, 0, partition - 1, k);
        } else {
            return quickSortSearch(nums, partition + 1, right, k);
        }
    }

    private int partition(int[] nums, int left, int right) {
        int index = left+1;
        for (int i = left+1; i <= right; i++) {
            if (nums[i] > nums[left]) {
                // if sort ，need swap
                swap(nums, i, index);
                index++;
            }
        }
        if (index-1 == left) {
            return left;
        }
        swap(nums, left, index-1);
        return index;
    }

    private void swap(int[] nums, int i, int index) {
        int tmp = nums[i];
        nums[i] = nums[index];
        nums[index] = tmp;
    }
}
//leetcode submit region end(Prohibit modification and deletion)
