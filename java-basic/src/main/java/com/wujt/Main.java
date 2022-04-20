package com.wujt;

/**
 * @author gow
 * @date 2022/1/15
 */
public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        int maxSubSeq = main.maxSubSeq(new int[]{1, 2, 3, 4, 2}, 6);
        System.out.println(maxSubSeq);
        String cryptStr = main.cryptStr('1', "password__a12345678_timeout_100");
        System.out.println(cryptStr);
    }

    public String cryptStr(char ch, String cmdStr) {
        // 命令字由单个字符或者“”字符串，
        // 需要寻找ch 出现的非“”的字符和“”中字符串
        // 替换其为******
        // 剔除多余的_

        String[] strings = cmdStr.split("_");
        StringBuilder builder = new StringBuilder();
        String replaceStr = "******";
        boolean exist = false;
        for (String cmd : strings) {
            if (cmd.equals("")) { // 空字符串
                continue;
            }

            char[] chars = cmd.toCharArray();
            boolean match = false;
            if (cmd.charAt(0) == '"') {  // 如果字符串startWith "

                for (int j = 1; j < chars.length - 1; j++) {
                    if (chars[j] == ch) {
                        match = true;
                        exist = true;
                        break;
                    }
                }

            } else {

                for (char aChar : chars) {
                    if (aChar == ch) { // 匹配
                        match = true;
                        exist = true;
                        break;
                    }
                }
                if (match) {
                    builder.append(replaceStr);
                } else {
                    builder.append(cmd);
                }
            }
            builder.append("_");
        }
        if (exist) {
            String toString = builder.toString();
            return toString.substring(0, toString.length() - 1);
        }

        return "ERROR";
    }

    public int maxSubSeq(int[] arr, int sum) {
        // 数组元素为正整数，子序列和一定递增
        // 遍历数组，判断连续子序列和是否为sum,等于更新result

        int result = -1;
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] < sum) {
                int currSum = arr[i];
                for (int j = i + 1; j < arr.length; j++) {
                    currSum += arr[j];
                    // 如果子序列和为sum
                    if (currSum == sum) {
                        int subLen = j - i + 1;
                        if (subLen > result) {
                            result = subLen;
                        }
                        break;
                    } else if (currSum > sum) { // 如果大于则跳出循环
                        break;
                    }
                }
            } else if (arr[i] == sum) {
                if (result < 1) {
                    result = 1;
                }
            }
        }
        // 特殊值处理
        if (arr[arr.length - 1] == sum) {
            if (result < 1) {
                result = 1;
            }
        }
        return result;
    }
}
