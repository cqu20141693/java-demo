package com.wujt.字符串;

import java.util.HashMap;

/**
 * @author wujt
 */
public class 重复子字符串 {
    public static void main(String[] args) {

        String maxSubString = getMaxSubString("abdabdcc");
        System.out.println(maxSubString);
    }

    public static String getMaxSubString(String data) {
        if (data == null || data.length() < 2) {
            return data;
        }
        String res = "";
        HashMap<Character, Integer> table = new HashMap();
        // 当遇到重复字符后，需要上一个出现字符后进行查找，所以需要记录字符的位置
        for (int i = 0; i < data.length() - 1; ) {
            for (int j = i; j < data.length(); j++) {
                Integer index = table.get(data.charAt(j));
                if (index != null) {
                    String sub = data.substring(i, j);
                    if (sub.length() > res.length()) {
                        res = sub;
                    }
                    i = index + 1;
                    break;
                } else {
                    table.put(data.charAt(j), j);
                }
                //如果比较到了最后一位都没有出现重复，则比较完成
                if (j == data.length() - 1) {
                    String sub = data.substring(i);
                    if (sub.length() > res.length()) {
                        res = sub;
                    }
                    i = data.length();
                    break;
                }
            }
            //清理table 进行下次循环；
            table.clear();
        }
        return res;
    }
}
