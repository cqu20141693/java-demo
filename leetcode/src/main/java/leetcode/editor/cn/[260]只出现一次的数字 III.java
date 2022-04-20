package leetcode.editor.cn;
//给定一个整数数组 nums，其中恰好有两个元素只出现一次，其余所有元素均出现两次。 找出只出现一次的那两个元素。你可以按 任意顺序 返回答案。 
//
// 
//
// 进阶：你的算法应该具有线性时间复杂度。你能否仅使用常数空间复杂度来实现？ 
//
// 
//
// 示例 1： 
//
// 
//输入：nums = [1,2,1,3,2,5]
//输出：[3,5]
//解释：[5, 3] 也是有效的答案。
// 
//
// 示例 2： 
//
// 
//输入：nums = [-1,0]
//输出：[-1,0]
// 
//
// 示例 3： 
//
// 
//输入：nums = [0,1]
//输出：[1,0]
// 
//
// 提示： 
//
// 
// 2 <= nums.length <= 3 * 10⁴ 
// -2³¹ <= nums[i] <= 2³¹ - 1 
// 除两个只出现一次的整数外，nums 中的其他数字都出现两次 
// 
// Related Topics 位运算 数组 👍 441 👎 0


//leetcode submit region begin(Prohibit modification and deletion)
class Solution {
    public int[] singleNumber(int[] nums) {

        // 异或结论： A~A=0;
        // 0异或任何数等于其本身
        // 该题得到的结论：所有值异或后必定出现一个标志位是1(只出现一次的),区分两个数
        // 再对数组进行遍历，根据div位数进行区分异或（类似只出现一次数字 1）

        int sum = 0;
        for (int num : nums) {
            sum ^= num;
        }
        int div = 1;
        for (int i = 0; i < 32; i++) {
            if (((sum >> i & 1) == 1)) {
                div <<= i;
                break;
            }
        }
        int a = 0, b = 0;
        for (int num : nums) {
            if ((num & div) != 0) {
                a ^= num;
            } else {
                b ^= num;
            }
        }
        return new int[]{a, b};
    }
}
//leetcode submit region end(Prohibit modification and deletion)
