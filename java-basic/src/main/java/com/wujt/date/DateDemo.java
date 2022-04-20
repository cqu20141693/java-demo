package com.wujt.date;

import java.time.Clock;
import java.util.Date;

/**
 * @author wujt  2021/5/12
 */
public class DateDemo {
    /**
     *
     * @date 2021/5/12 15:37
     * @param args
     */
    public static void main(String[] args) {
        Date date = new Date();
        long time = date.getTime();
        long l = time / 1000;
        long floor =(long) Math.floor((time / 1000) * 1000);
        System.out.println(time+":"+l+":"+floor);
    }
}
