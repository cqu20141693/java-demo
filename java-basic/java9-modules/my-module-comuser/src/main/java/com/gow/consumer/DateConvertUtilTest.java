package com.gow.consumer;

import com.gow.supplier.time.DateTimeConverterUtil;
import org.junit.Test;

import java.time.*;
import java.util.Date;

/**
 * @author gow 2021/06/12
 */
public class DateConvertUtilTest {
    public static void main(String[] args) {
        DateConvertUtilTest dateConvertUtilTest = new DateConvertUtilTest();
        dateConvertUtilTest.dateConverterTest();
    }

    public void dateConverterTest() {
        Date date = new Date();

        LocalDateTime localDateTime = DateTimeConverterUtil.toLocalDateTime(date);
        assert localDateTime != null;

        LocalDate localDate = DateTimeConverterUtil.toLocalDate(date);
        assert localDate != null;

        LocalTime localTime = DateTimeConverterUtil.toLocalTime(date);
        assert localTime != null;

        Instant instant = DateTimeConverterUtil.toInstant(date);
        assert instant != null;
    }

    @Test
    public void localDateTimeConverterTest() {
        LocalDateTime ldt = LocalDateTime.now();

        Date date = DateTimeConverterUtil.toDate(ldt);
        assert date != null;

        LocalDate localDate = DateTimeConverterUtil.toLocalDate(ldt);
        assert localDate != null;

        LocalTime localTime = DateTimeConverterUtil.toLocalTime(ldt);
        assert localTime != null;

        Instant instant = DateTimeConverterUtil.toInstant(ldt);
        assert instant != null;

        ZonedDateTime zonedDateTime = DateTimeConverterUtil.toZonedDateTime(ldt);
        assert zonedDateTime != null;
    }

    public void localDateConverterTest() {
        LocalDate ld = LocalDate.now();
        Date date = DateTimeConverterUtil.toDate(ld);


        LocalDateTime localDateTime = DateTimeConverterUtil.toLocalDateTime(ld);


        Instant instant = DateTimeConverterUtil.toInstant(ld);
    }


    public void localTimeConverterTest() {
        LocalTime lt = LocalTime.now();

        Date date = DateTimeConverterUtil.toDate(lt);


        LocalDateTime localDateTime = DateTimeConverterUtil.toLocalDateTime(lt);


        Instant instant = DateTimeConverterUtil.toInstant(lt);

    }


    public void instantConverterTest() {
        Instant instant = Instant.now();
        Date date = DateTimeConverterUtil.toDate(instant);


        LocalDateTime localDateTime = DateTimeConverterUtil.toLocalDateTime(instant);


        LocalDate localDate = DateTimeConverterUtil.toLocalDate(instant);

    }


    public void zonedDateTimeConverterTest() {
        //ToOther
        ZonedDateTime zonedDateTime = ZonedDateTime.now();

        Date date = DateTimeConverterUtil.toDate(zonedDateTime);


        LocalDateTime localDateTime = DateTimeConverterUtil.toLocalDateTime(zonedDateTime);


        LocalDate localDate = DateTimeConverterUtil.toLocalDate(zonedDateTime);


        LocalTime localTime = DateTimeConverterUtil.toLocalTime(zonedDateTime);


        Instant instant = DateTimeConverterUtil.toInstant(zonedDateTime);


        //toZonedDateTime
        ZonedDateTime zonedDateTime1 = DateTimeConverterUtil.toZonedDateTime(new Date());


        ZonedDateTime zonedDateTime2 = DateTimeConverterUtil.toZonedDateTime(LocalDateTime.now());


        ZonedDateTime zonedDateTime3 = DateTimeConverterUtil.toZonedDateTime(LocalDate.now());


        ZonedDateTime zonedDateTime4 = DateTimeConverterUtil.toZonedDateTime(LocalTime.now());


        ZonedDateTime zonedDateTime5 = DateTimeConverterUtil.toZonedDateTime(Instant.now());


        //Asia/Shanghai
        ZonedDateTime zonedDateTime6 = DateTimeConverterUtil.toZonedDateTime(LocalDateTime.now(), "Asia/Shanghai");

        //Asia/Shanghai转Europe/Paris
        ZonedDateTime zonedDateTime7 = DateTimeConverterUtil.toZonedDateTimeAndTransformZone(LocalDateTime.now(), "Europe/Paris");
        ZonedDateTime zonedDateTime8 = DateTimeConverterUtil.toZonedDateTime(new Date(), "Europe/Paris");
    }

}
