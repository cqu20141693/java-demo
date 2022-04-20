package com.wujt;

import java.util.ArrayList;

/**
 * @author gow
 * @date 2022/1/12
 */
public class Solution {
    /**
     * 反转字符串
     *
     * @param str string字符串
     * @return string字符串
     */
    public String solve(String str) {
        // write code here
        int length = str.length();
        if (str == null || length < 2) {
            return str;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = length - 1; i >= 0; i--) {
            builder.append(str.charAt(i));
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        String solve = solution.solve("abcd");
        System.out.println(solve);

        ArrayList<String> strings = solution.restoreIpAddresses("25525522135");
        System.out.println(strings);
    }

    public ArrayList<String> restoreIpAddresses(String s) {
        // write code here
        // write code here
        ArrayList<String> list = new ArrayList<>();
        for (int a = 1; a < 4; a++) {
            for (int b = 1; b < 4; b++) {
                for (int c = 1; c < 4; c++) {
                    for (int d = 1; d < 4; d++) {
                        if (a + b + c + d == s.length()) {
                            String first = s.substring(0,a);
                            String second = s.substring(a, a + b);
                            String third = s.substring(a + b, a + b + c);
                            String fourth = s.substring(a + b + c, a + b + c + d);
                            if (check(first) && check(second) && check(third) && check(fourth)) {
                                list.add(first + "." + second + "." + third + "." + fourth);
                            }
                        }
                    }
                }
            }
        }
        return list;
    }

    private boolean check(String str) {
        if (Integer.parseInt(str) <= 255) {
            if (str.charAt(0) != '0') {
                return true;
            } else {
                return str.length() == 1;
            }
        }
        return false;
    }
}
