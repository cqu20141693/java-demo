package leetcode.editor.cn;
//编写一个函数，其作用是将输入的字符串反转过来。输入字符串以字符数组 s 的形式给出。 
//
// 不要给另外的数组分配额外的空间，你必须原地修改输入数组、使用 O(1) 的额外空间解决这一问题。 
//
// 
//
// 示例 1： 
//
// 
//输入：s = ["h","e","l","l","o"]
//输出：["o","l","l","e","h"]
// 
//
// 示例 2： 
//
// 
//输入：s = ["H","a","n","n","a","h"]
//输出：["h","a","n","n","a","H"] 
//
// 
//
// 提示： 
//
// 
// 1 <= s.length <= 10⁵ 
// s[i] 都是 ASCII 码表中的可打印字符 
// 
// Related Topics 递归 双指针 字符串 👍 512 👎 0


//leetcode submit region begin(Prohibit modification and deletion)
class Solution {

    /**
     * 方式一，用一个新的字符数组进行接受字符串翻转数据
     * 方式二：
     * 字符串得到一个字符数组，然后用双指针方法将头尾进行对调
     *
     *
     * @param s
     */
    public void reverseString(char[] s) {
        if(s==null|| s.length<2){
            return;
        }
        int n=s.length;
        for (int l=0,r=n-1;l<r;l++,r--){
            char temp=s[l];
            s[l]=s[r];
            s[r]=temp;
        }
    }
}
//leetcode submit region end(Prohibit modification and deletion)
