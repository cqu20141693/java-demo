package leetcode.editor.cn;
//给出集合 [1,2,3,...,n]，其所有元素共有 n! 种排列。 
//
// 按大小顺序列出所有排列情况，并一一标记，当 n = 3 时, 所有排列如下： 
//
// 
// "123" 
// "132" 
// "213" 
// "231" 
// "312" 
// "321" 
// 
//
// 给定 n 和 k，返回第 k 个排列。 
//
// 
//
// 示例 1： 
//
// 
//输入：n = 3, k = 3
//输出："213"
// 
//
// 示例 2： 
//
// 
//输入：n = 4, k = 9
//输出："2314"
// 
//
// 示例 3： 
//
// 
//输入：n = 3, k = 1
//输出："123"
// 
//
// 
//
// 提示： 
//
// 
// 1 <= n <= 9 
// 1 <= k <= n! 
// 
// Related Topics 递归 数学 👍 569 👎 0


//leetcode submit region begin(Prohibit modification and deletion)
class Solution {
    public String getPermutation(int n, int k) {
        // 初始化排序数据
        int[] array = new int[n];
        array[0] = 1;
        for (int i = 1; i < n; i++) {
            array[i] = array[i - 1] * i;
        }

        // 计算第K个数据
        // 从左往右确定数字，判断k的值与各个排列值的大下 i*n!<k<(i+1)*n!
        // 则k的第n位数字的值为i+1;
        // 后续对m=k%(n!),j*(n-1)!<k<(j+1)*(n-1)! 直到n=1

        StringBuilder builder = new StringBuilder();
        int[] valid = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            valid[i] = 1;
        }
        k--; // 此处是为了保证第一次计算位数
        for (int i = 1; i <= n; i++) {
            int index = k / array[n - i] + 1; // 计算k所在n排列的区间
            for (int j = 1; j <= n; j++) {
                index -= valid[j];
                if (index == 0) {
                    builder.append(j);
                    valid[j] = 0;
                    break;
                }
            }
            k = k % array[n - i];
        }
        return builder.toString();
    }
}
//leetcode submit region end(Prohibit modification and deletion)
