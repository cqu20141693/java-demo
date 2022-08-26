package com.wujt.date;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * @author wujt
 */
public class LocalDateTimeDemo {
    public static void main(String[] args) {
        LocalDateTime currHour = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);
        LocalDateTime preHour = currHour.minusHours(1);
        Timestamp currHourStamp = new Timestamp(currHour.toInstant(ZoneOffset.of("+8")).toEpochMilli());
        Timestamp preHourStamp = new Timestamp(preHour.toInstant(ZoneOffset.of("+8")).toEpochMilli());


        testFormat();

        Instant parse = StringToInstant("2018-08-23" + " 00:00:00");
        System.out.println(parse.toEpochMilli());
    }

    public static final Instant StringToInstant(String string) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //将 string 装换成带有 T 的国际时间，但是没有进行，时区的转换，即没有将时间转为国际时间，只是格式转为国际时间
        LocalDateTime parse = LocalDateTime.parse(string, dateTimeFormatter);
        //+8 小时，offset 可以理解为时间偏移量
        ZoneOffset offset = OffsetDateTime.now().getOffset();
        //转换为 通过时间偏移量将 string -8小时 变为 国际时间，因为亚洲上海是8时区
        Instant instant = parse.toInstant(offset);
        return instant;
    }

    private static void testFormat() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse("2021-11-24 08:10:20", df);
        LocalDateTime end = LocalDateTime.parse("2021-11-24 20:10:20", df);
        long milli = start.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        long milli2 = end.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        System.out.println(milli + ":" + milli2);
        long epochMilli = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), ZoneOffset.of("+8"));
        LocalDateTime withNano = localDateTime.withMinute(0).withSecond(0).withNano(0);
        System.out.println(withNano);
        LocalDateTime time = withNano.plusHours(1);
        System.out.println(time);
        //2022/1/1
        LocalDateTime start1 = LocalDateTime.ofInstant(Instant.ofEpochMilli(1658048838352L), ZoneOffset.of("+8"));
        LocalDateTime start2 = LocalDateTime.ofInstant(Instant.ofEpochMilli(1640966400001L), ZoneOffset.of("+8"));
        LocalDateTime end2 = LocalDateTime.ofInstant(Instant.ofEpochMilli(1660727238352L), ZoneOffset.of("+8"));
        start1 = start1.plusMonths(1);
        start2 = start2.plusMonths(1);
        if (end2.isBefore(start2)) {
            System.out.println("before start2");
        }
        if (end2.isBefore(start1)) {
            System.out.println("before");
        }
    }
}
