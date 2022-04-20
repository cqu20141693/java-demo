package leetcode.editor.cn;

import java.util.ArrayList;

/**
 * @author gow
 * @date 2022/1/12
 */
public class strings数字字符串转IP {
    /**
     * 现在有一个只包含数字的字符串，将该字符串转化成IP地址的形式，返回所有可能的情况。
     * 例如：
     * 给出的字符串为"25525522135",
     * 返回["255.255.22.135", "255.255.221.35"]. (顺序没有关系)
     *
     * 数据范围：字符串长度 0 \le n \le 120≤n≤12
     * 要求：空间复杂度 O(n!)O(n!),时间复杂度 O(n!)O(n!)
     *
     * 注意：ip地址是由四段数字组成的数字序列，格式如 "x.x.x.x"，其中 x 的范围应当是 [0,255]。
     *
     * @param s string字符串
     * @return string字符串ArrayList
     */
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
