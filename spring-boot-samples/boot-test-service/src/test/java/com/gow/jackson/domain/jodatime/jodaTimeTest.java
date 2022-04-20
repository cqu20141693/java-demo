package com.gow.jackson.domain.jodatime;

import static com.gow.jackson.JacksonObject.toDoc;
import static com.gow.jackson.JacksonObject.toJson;
import static com.gow.jackson.JacksonObject.toObject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.gow.jackson.domain.jodatime.JodaTime;
import com.jayway.jsonpath.DocumentContext;
import java.io.IOException;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MonthDay;
import org.joda.time.YearMonth;


/**
 * @author gow
 * @date 2021/7/24
 */
public class jodaTimeTest {


    private static final DateTime DATE_TIME = new DateTime(2018, 1, 1, 14, 22, 33, 25, DateTimeZone.UTC);
    private static final Instant INSTANT = DATE_TIME.toInstant();
    private static final YearMonth YEAR_MONTH = new YearMonth(2018, 1);
    private static final MonthDay MONTH_DAY = new MonthDay(1, 14);
    private static final LocalDateTime LOCAL_DATE_TIME = new LocalDateTime(2018, 1, 1, 14, 22, 33, 25);
    private static final LocalDate LOCAL_DATE = LOCAL_DATE_TIME.toLocalDate();
    private static final LocalTime LOCAL_TIME = LOCAL_DATE_TIME.toLocalTime();

    public static void main(String[] args) throws IOException {

        dateTimeJsonToObject();
        dateTimeObjectToJson();
        instantJsonToObject();
        instantObjectToJson();
        yearMonthJsonToObject();
        yearMonthObjectToJson();
        monthDayJsonToObject();
        monthDayObjectToJson();
        localDateJsonToObject();
        localDateObjectToJson();
        localDateTimeJsonToObject();
        localDateTimeObjectToJson();
        localTimeJsonToObject();
        localTimeObjectToJson();
        customDateTimeJsonToObject();
        customDateTimeObjectToJson();
        customInstantJsonToObject();
        customInstantObjectToJson();
        customYearMonthJsonToObject();
        customYearMonthObjectToJson();
        customMonthDayJsonToObject();
        customMonthDayObjectToJson();
        customLocalDateJsonToObject();
        customLocalDateObjectToJson();
        customLocalDateTimeJsonToObject();
        customLocalDateTimeObjectToJson();
        customLocalTimeJsonToObject();
        customLocalTimeObjectToJson();
    }

    public static void dateTimeObjectToJson() throws JsonProcessingException {
        JodaTime object = new JodaTime();
        object.setIsoDateTime(DATE_TIME);
        String json = toJson(object);
        System.out.println("DateTime:" + json);
        DocumentContext doc = toDoc(json);
        assertEquals("2018-01-01T14:22:33.025Z", doc.read("$.isoDateTime", String.class));
    }

    public static void dateTimeJsonToObject() throws IOException {
        String json = "{\"isoDateTime\":\"2018-01-01T14:22:33.025Z\"}";
        JodaTime object = toObject(json, JodaTime.class);
        assertEquals(DATE_TIME, object.getIsoDateTime());
    }

    public static void instantObjectToJson() throws JsonProcessingException {
        JodaTime object = new JodaTime();
        object.setIsoInstant(INSTANT);
        String json = toJson(object);
        DocumentContext doc = toDoc(json);
        assertEquals("2018-01-01T14:22:33.025Z", doc.read("$.isoInstant", String.class));
    }

    public static void instantJsonToObject() throws IOException {
        String json = "{\"isoInstant\":\"2018-01-01T14:22:33.025Z\"}";
        JodaTime object = toObject(json, JodaTime.class);
        assertEquals(INSTANT, object.getIsoInstant());
    }

    public static void yearMonthObjectToJson() throws JsonProcessingException {
        JodaTime object = new JodaTime();
        object.setIsoYearMonth(YEAR_MONTH);
        String json = toJson(object);
        DocumentContext doc = toDoc(json);
        assertEquals("2018-01", doc.read("$.isoYearMonth", String.class));
    }

    public static void yearMonthJsonToObject() throws IOException {
        String json = "{\"isoYearMonth\":\"2018-01\"}";
        JodaTime object = toObject(json, JodaTime.class);
        assertEquals(YEAR_MONTH, object.getIsoYearMonth());
    }

    public static void monthDayObjectToJson() throws JsonProcessingException {
        JodaTime object = new JodaTime();
        object.setIsoMonthDay(MONTH_DAY);
        String json = toJson(object);
        DocumentContext doc = toDoc(json);
        assertEquals("--01-14", doc.read("$.isoMonthDay", String.class));
    }

    public static void monthDayJsonToObject() throws IOException {
        String json = "{\"isoMonthDay\":\"--01-14\"}";
        JodaTime object = toObject(json, JodaTime.class);
        assertEquals(MONTH_DAY, object.getIsoMonthDay());
    }

    public static void localDateTimeObjectToJson() throws JsonProcessingException {
        JodaTime object = new JodaTime();
        object.setIsoLocalDateTime(LOCAL_DATE_TIME);
        String json = toJson(object);
        DocumentContext doc = toDoc(json);
        assertEquals("2018-01-01T14:22:33.025", doc.read("$.isoLocalDateTime", String.class));
    }

    public static void localDateTimeJsonToObject() throws IOException {
        String json = "{\"isoLocalDateTime\":\"2018-01-01T14:22:33.025\"}";
        JodaTime object = toObject(json, JodaTime.class);
        assertEquals(LOCAL_DATE_TIME, object.getIsoLocalDateTime());
    }

    public static void localDateObjectToJson() throws JsonProcessingException {
        JodaTime object = new JodaTime();
        object.setIsoLocalDate(LOCAL_DATE);
        String json = toJson(object);
        DocumentContext doc = toDoc(json);
        assertEquals("2018-01-01", doc.read("$.isoLocalDate", String.class));
    }

    public static void localDateJsonToObject() throws IOException {
        String json = "{\"isoLocalDate\":\"2018-01-01\"}";
        JodaTime object = toObject(json, JodaTime.class);
        assertEquals(LOCAL_DATE, object.getIsoLocalDate());
    }

    public static void localTimeObjectToJson() throws JsonProcessingException {
        JodaTime object = new JodaTime();
        object.setIsoLocalTime(LOCAL_TIME);
        String json = toJson(object);
        DocumentContext doc = toDoc(json);
        assertEquals("14:22:33.025", doc.read("$.isoLocalTime", String.class));
    }

    public static void localTimeJsonToObject() throws IOException {
        String json = "{\"isoLocalTime\":\"14:22:33.025\"}";
        JodaTime object = toObject(json, JodaTime.class);
        assertEquals(LOCAL_TIME, object.getIsoLocalTime());
    }

    public static void customDateTimeObjectToJson() throws JsonProcessingException {
        JodaTime object = new JodaTime();
        object.setCustomDateTime(DATE_TIME);
        String json = toJson(object);
        DocumentContext doc = toDoc(json);
        assertEquals("01/01/2018 14_22_33_025|UTC", doc.read("$.customDateTime", String.class));
    }

    public static void customDateTimeJsonToObject() throws IOException {
        String json = "{\"customDateTime\":\"01/01/2018 14_22_33_025|UTC\"}";
        JodaTime object = toObject(json, JodaTime.class);
        assertEquals(DATE_TIME, object.getCustomDateTime());
    }

    public static void customInstantObjectToJson() throws JsonProcessingException {
        JodaTime object = new JodaTime();
        object.setCustomInstant(INSTANT);
        String json = toJson(object);
        DocumentContext doc = toDoc(json);
        assertEquals("01/01/2018 14_22_33_025|UTC", doc.read("$.customInstant", String.class));
    }

    public static void customInstantJsonToObject() throws IOException {
        String json = "{\"customInstant\":\"01/01/2018 14_22_33_025|UTC\"}";
        JodaTime object = toObject(json, JodaTime.class);
        assertEquals(INSTANT, object.getCustomInstant());
    }

    public static void customYearMonthObjectToJson() throws JsonProcessingException {
        JodaTime object = new JodaTime();
        object.setCustomYearMonth(YEAR_MONTH);
        String json = toJson(object);
        DocumentContext doc = toDoc(json);
        assertEquals("2018/01", doc.read("$.customYearMonth", String.class));
    }

    public static void customYearMonthJsonToObject() throws IOException {
        String json = "{\"customYearMonth\":\"2018/01\"}";
        JodaTime object = toObject(json, JodaTime.class);
        assertEquals(YEAR_MONTH, object.getCustomYearMonth());
    }

    public static void customMonthDayObjectToJson() throws JsonProcessingException {
        JodaTime object = new JodaTime();
        object.setCustomMonthDay(MONTH_DAY);
        String json = toJson(object);
        DocumentContext doc = toDoc(json);
        assertEquals("01_14", doc.read("$.customMonthDay", String.class));
    }

    public static void customMonthDayJsonToObject() throws IOException {
        String json = "{\"customMonthDay\":\"01_14\"}";
        JodaTime object = toObject(json, JodaTime.class);
        assertEquals(MONTH_DAY, object.getCustomMonthDay());
    }

    public static void customLocalDateTimeObjectToJson() throws JsonProcessingException {
        JodaTime object = new JodaTime();
        object.setCustomLocalDateTime(LOCAL_DATE_TIME);
        String json = toJson(object);
        DocumentContext doc = toDoc(json);
        assertEquals("01/01/2018 14_22_33_025", doc.read("$.customLocalDateTime", String.class));
    }

    public static void customLocalDateTimeJsonToObject() throws IOException {
        String json = "{\"customLocalDateTime\":\"01/01/2018 14_22_33_025\"}";
        JodaTime object = toObject(json, JodaTime.class);
        assertEquals(LOCAL_DATE_TIME, object.getCustomLocalDateTime());
    }

    public static void customLocalDateObjectToJson() throws JsonProcessingException {
        JodaTime object = new JodaTime();
        object.setCustomLocalDate(LOCAL_DATE);
        String json = toJson(object);
        DocumentContext doc = toDoc(json);
        assertEquals("01/01/2018", doc.read("$.customLocalDate", String.class));
    }

    public static void customLocalDateJsonToObject() throws IOException {
        String json = "{\"customLocalDate\":\"01/01/2018\"}";
        JodaTime object = toObject(json, JodaTime.class);
        assertEquals(LOCAL_DATE, object.getCustomLocalDate());
    }

    public static void customLocalTimeObjectToJson() throws JsonProcessingException {
        JodaTime object = new JodaTime();
        object.setCustomLocalTime(LOCAL_TIME);
        String json = toJson(object);
        DocumentContext doc = toDoc(json);
        assertEquals("14_22_33_025", doc.read("$.customLocalTime", String.class));
    }

    public static void customLocalTimeJsonToObject() throws IOException {
        String json = "{\"customLocalTime\":\"14_22_33_025\"}";
        JodaTime object = toObject(json, JodaTime.class);
        assertEquals(LOCAL_TIME, object.getCustomLocalTime());
    }

}
