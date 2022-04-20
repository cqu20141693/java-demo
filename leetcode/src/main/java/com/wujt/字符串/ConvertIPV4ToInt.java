package com.wujt.字符串;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wujt
 */
public class ConvertIPV4ToInt {

    public static void main(String[] args) {

        System.out.println(ipv4ToInt("0.0.0.0"));
        System.out.println(ipv4ToInt("0.0.0.1"));
        System.out.println(ipv4ToInt("0.0.0.255"));
        System.out.println(ipv4ToInt("0.0.1.0"));
        System.out.println(ipv4ToInt("128.0.0.0"));
    }
    private static Integer ipv4ToInt(String ip) {
        if (isIPv4Address(ip)) {
            String[] splits = ip.split("\\.");
            int result = 0;
            for (int i = 0; i < splits.length; i++) {
                int value = Integer.parseInt(splits[i]) << 8*(3 - i);
                // 利用或提高性能，1位不会冲突
                result |= value;
            }
            return result;
        } else {
            return null;
        }
    }

    /**
     * 判断是否为ipv4地址
     */
    private static boolean isIPv4Address(String ipv4Addr) {
        // 0-255的数字
        String lower = "(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])";
        String regex = lower + "(\\." + lower + "){3}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(ipv4Addr);
        return matcher.matches();
    }

    private int convertIPV4ToInt(String ip) throws Exception {
        String[] list = new String[4];
        // 验证并获取每一个段数据
        if (validate(ip, list)) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < list.length; i++) {
                Integer tmp = Integer.parseInt(list[i]);
                builder.append(Integer.toBinaryString(tmp).substring(25));
            }
            return getResult(builder.toString());
        } else {
            throw new Exception("ip is invalid");
        }

    }

    private int getResult(String binaryString) {
        int ret = 0;
        if (binaryString.charAt(0) == 0) {
            for (int i = 1; i < binaryString.length(); i++) {
                if (binaryString.charAt(i) == 1) {
                    ret = ret + (1 << (32 - i));
                }
            }
            return ret;
        } else {
            // 取反加1
            for (int i = 1; i < binaryString.length(); i++) {
                if (binaryString.charAt(i) == 0) {
                    ret = ret + (1 << (32 - i));
                }
            }
            return -ret - 1;
        }

    }

    private boolean validate(String ip, String[] list) {
        // 正则
        String[] ips = ip.split(" |\\.");
        int count = 0;
        for (int i = 0; i < ips.length; i++) {
            if (ips[i].length() != 0) {
                list[count++] = ips[i];
            }
        }
        if (list.length != 4) {
            return false;
        }
        return true;
    }
}
