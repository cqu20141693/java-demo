package com.wujt.date;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.time.LocalTime;
import lombok.Data;
import lombok.ToString;

/**
 * @author gow
 * @date 2021/11/24
 */
public class LocalTimeDemo {

    @Data
    @ToString
    static class TestTime{
        private LocalTime start;
        private LocalTime end;
        private String unit;
    }
    public static void main(String[] args) {

        LocalTime start = LocalTime.now().withNano(0);
        String startStr = start.toString();

        LocalTime end = start.plusHours(3);
        String endStr = end.toString();

        System.out.println(startStr + " - " + endStr);
        LocalTime now = LocalTime.now();

        if (now.isAfter(start) && now.isBefore(end)) {
            System.out.println("check success");
        }

        TestTime testTime = new TestTime();
        testTime.setStart(start);
        testTime.setEnd(end);
        testTime.setUnit("");
        String jsonString = JSONObject.toJSONString(testTime);

        TestTime parse = JSON.parseObject(jsonString, TestTime.class);
        System.out.println(jsonString+" - "+parse);



    }
}
