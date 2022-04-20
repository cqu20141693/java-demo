package com.wujt.dynamic_program;

/**
 * 最长回文字符串
 *
 * @author wujt
 */
public class LongestPalindrome {

    /**
     * 使用 s.charAt(i)获取字符串的指定位置字符
     *
     * @param s
     * @return
     */
    public String longestPalindrome(String s) {
        // 思路1 : 因为我们知道回文串的中心要么是i,要么是i,i+1;
        // 而每一个中心节点的状态转移方程为P(i,j)=P(i+1,j−1)∧(Si​==Sj)
        // 将当前点为中心的回文串比较当前最大的回文串，如果大于则重新计算

        if (s == null || s.length() == 0) {
            return "";
        } else if (s.length() == 1) {
            return s;
        } else {
            int start = 0, end = 0;
                for (int i = 0; i < s.length(); i++) {
                    int len1 = expandAroundCenter(s, i, i);
                    int len2 = expandAroundCenter(s, i, i + 1);
                    int len = Math.max(len1, len2);
                    if (len > end - start+1) {
                        // 计算start 和end, 中心点+ - (len)/2
                        start = i - (len - 1) / 2;
                        end = i + len / 2;
                    }
            }
            return s.substring(start, end + 1);
        }


    }

    /**
     * 中心扩展状态转移寻找是否为回文字符串
     *
     * @param s
     * @param left  当left>0
     * @param right rigth>s.length
     *              and s.charAt(left) == s.charAt(right) 时，可以转移
     * @return
     */
    public int expandAroundCenter(String s, int left, int right) {
        //
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            --left;
            ++right;
        }
        return right - left - 1;
    }
}
