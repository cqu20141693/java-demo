package com.gow.supplier.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author gow 2021/06/12
 */
public class RegexResourceUtil {
    public static String[] matcher(Pattern pattern, String text) throws Exception {
        String[] result = new String[99];
        int index = 0;
        Matcher match = pattern.matcher(text);
        while (match.find()) {
            result[index] = match.group();
            index++;
        }
        return result;
    }

    public static String matcher(String rule, String text) throws Exception {
        String result = "";
        Pattern pattern = Pattern.compile(rule);
        Matcher match = pattern.matcher(text);
        while (match.find()) {
            result = match.group();
        }
        return result;
    }

    public static void main(String[] args) throws Exception {

        System.out.println(matcher("\\d+(分钟|分|min)[以之]?[前后]", "10分后"));
    }
}
