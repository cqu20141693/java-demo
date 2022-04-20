package com.wujt.date;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
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
    }
}
