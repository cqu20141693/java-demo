package com.gow.supplier;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author gow 2021/06/12
 */
public class DateUtils {
    public static final String DATE_FORMAT_FULL = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_FORMAT_SHORT = "yyyy-MM-dd";

    public static final String DATE_FORMAT_COMPACT = "yyyyMMdd";

    public static final String DATE_FORMAT_COMPACTFULL = "yyyyMMddHHmmss";

    public static final String DATE_FORMAT_FULL_MSEL = "yyyyMMddHHmmssSSSS";

    public static final String DATE_YEAR_MONTH = "yyyyMM";

    public static final String DATE_FORMAT_FULL_MSE = "yyyyMMddHHmmssSSS";

    /**
     * 获取系统当前日期
     *
     * @return
     */
    public static Date getCurrentDate() {
        return new Date();
    }

    /**
     * 获取系统当前日期
     *
     * @return
     */
    public static LocalDateTime getCurrentLocalDateTime() {
        return LocalDateTime.now();
    }

    /**
     * 根据时间格式返回对应的String类型的时间
     *
     * @param format
     * @return
     */
    public static String getCurDateTime(String format) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        String dataTime = now.format(dateTimeFormatter);
        return dataTime;
    }

    /**
     * 得到当前日期
     *
     * @return String 当前日期 yyyy-MM-dd HH:mm:ss格式
     * @author kevin
     */
    public static String getCurDateTime() {
        return getCurDateTime(DATE_FORMAT_FULL);
    }

    /**
     * 得到当前日期
     *
     * @return String 当前日期 yyyyMMddHHmmss格式
     * @author kevin
     */
    public static String getCurDateTimeCompact() {
        return getCurDateTime(DATE_FORMAT_COMPACTFULL);
    }

    /**
     * @return String 当前日期 yyyyMM
     * @author kevin
     */
    public static String getCurYearMonth() {
        return getCurDateTime(DATE_YEAR_MONTH);
    }

    /**
     * 得到当前日期
     *
     * @return String 当前日期 yyyy-MM-dd
     * @author kevin
     */
    public static String getCurDate() {
        return getCurDateTime(DATE_FORMAT_SHORT);
    }

    /**
     * 判断是否是今天
     *
     * @param strDate
     * @return
     */
    public static boolean isCurrentDay(String strDate) {
        LocalDate strLocalDate = LocalDate.parse(strDate);
        if (LocalDate.now().getYear() == strLocalDate.getYear()) {
            MonthDay monthDay = MonthDay.from(strLocalDate);
            MonthDay today = MonthDay.from(LocalDate.now());
            return monthDay.equals(today);
        }
        return false;
    }

    /**
     * 日期转字符串
     *
     * @return String
     * @author kevin
     */
    public static String parseDateToString(Date thedate, String format) {
        if (thedate != null) {
            Instant instant = thedate.toInstant();
            ZoneId zone = ZoneId.systemDefault();
            LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
            return localDateTime.format(dateTimeFormatter);
        }
        return null;
    }

}
