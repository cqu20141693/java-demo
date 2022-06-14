package com.cc.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * wcc 2022/6/6
 */
public class StringUtils {
    private static final Map<String, Pattern> PATTERN_CACHE = new ConcurrentHashMap();

    public static boolean isNullOrEmpty(Object obj) {
        return obj == null || "".equals(obj.toString());
    }

    public static boolean isInt(Object obj) {
        if (isNullOrEmpty(obj)) {
            return false;
        } else {
            return obj instanceof Integer ? true : obj.toString().matches("[-+]?\\d+");
        }
    }

    public static boolean isDouble(Object obj) {
        if (isNullOrEmpty(obj)) {
            return false;
        } else {
            return !(obj instanceof Double) && !(obj instanceof Float) ? compileRegex("[-+]?\\d+\\.\\d+").matcher(obj.toString()).matches() : true;
        }
    }

    public static final Pattern compileRegex(String regex) {
        Pattern pattern = (Pattern) PATTERN_CACHE.get(regex);
        if (pattern == null) {
            pattern = Pattern.compile(regex);
            PATTERN_CACHE.put(regex, pattern);
        }

        return pattern;
    }

    public static boolean isNumber(Object obj) {
        if (obj instanceof Number) {
            return true;
        } else {
            return isInt(obj) || isDouble(obj);
        }
    }

    public static String throwable2String(Throwable e) {
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }

}
