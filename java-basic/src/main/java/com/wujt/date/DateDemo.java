package com.wujt.date;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * @author wujt  2021/5/12
 */
public class DateDemo {
    /**
     * @param args
     * @date 2021/5/12 15:37
     */
    public static void main(String[] args) {
        Date date = new Date();
        long time = date.getTime();
        long l = time / 1000;
        long floor = (long) Math.floor((time / 1000) * 1000);
        System.out.println(time + ":" + l + ":" + floor);


        LocalDateTime startTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(1L), ZoneOffset.of("+8"));
        System.out.println(startTime);
        System.out.println(startTime.toLocalDate().toString());
    }
}
