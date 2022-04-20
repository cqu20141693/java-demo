package com.gow.cosumer;

import com.gow.supplier.time.DateTimeConverterUtil;

import java.util.Date;

/**
 * @author gow 2021/06/12
 */
public class DateConvertUtilsApp {
    public static void main(String[] args) {
        long epochMilli = DateTimeConverterUtil.toEpochMilli(new Date());
        System.out.println(epochMilli);
    }
}
