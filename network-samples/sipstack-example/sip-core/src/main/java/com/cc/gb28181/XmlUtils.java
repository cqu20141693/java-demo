package com.cc.gb28181;

public class XmlUtils {

    public static String safeString(Object val) {
        if (val == null) {
            return "";
        }

        return val.toString();
    }
}
