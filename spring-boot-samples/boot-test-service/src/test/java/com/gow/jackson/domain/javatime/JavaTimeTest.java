package com.gow.jackson.domain.javatime;

import static com.gow.jackson.JacksonObject.toDoc;
import static com.gow.jackson.JacksonObject.toJson;
import static com.gow.jackson.JacksonObject.toObject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.gow.jackson.domain.javatime.JavaTime;
import com.jayway.jsonpath.DocumentContext;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author gow
 * @date 2021/7/24
 */
public class JavaTimeTest {

    private LocalDateTime LOCAL_DATE_TIME = LocalDateTime.of(2018, 1, 1, 14, 30, 22, 125_000_000);
    private LocalDate LOCAL_DATE = LOCAL_DATE_TIME.toLocalDate();
    private LocalTime LOCAL_TIME = LOCAL_DATE_TIME.toLocalTime();
    private Year YEAR = Year.of(2018);
    private YearMonth YEAR_MONTH = YearMonth.of(2018, 1);
    private MonthDay MONTH_DAY = MonthDay.of(1, 1);
    private ZonedDateTime ZONED_DATE_TIME =
            ZonedDateTime.of(2018, 1, 1, 14, 30, 22, 125_000_000, ZoneId.of("Europe/Madrid"));
    private OffsetDateTime
            OFFSET_DATE_TIME = OffsetDateTime.of(2018, 1, 1, 14, 30, 22, 125_000_000, ZoneOffset.ofHoursMinutes(2, 30));
    private OffsetTime OFFSET_TIME = OFFSET_DATE_TIME.toOffsetTime();
    private Instant INSTANT = ZONED_DATE_TIME.toInstant();


    @Test
    @DisplayName("LocalDateTime conversion object to json")
    public void localDateTimeObjectToJson() throws JsonProcessingException {
        JavaTime object = new JavaTime();
        object.setIsoLocalDateTime(LOCAL_DATE_TIME);
        String json = toJson(object);
        DocumentContext doc = toDoc(json);
        assertEquals("2018-01-01T14:30:22.125", doc.read("$.isoLocalDateTime", String.class));
    }

    @Test
    @DisplayName("LocalDateTime conversion json to object")
    public void localDateTimeJsonToObject() throws IOException {
        String json = "{\"isoLocalDateTime\":\"2018-01-01T14:30:22.125\"}";
        JavaTime object = toObject(json, JavaTime.class);
        assertEquals(LOCAL_DATE_TIME, object.getIsoLocalDateTime());
    }

    @Test
    @DisplayName("LocalDate conversion object to json")
    public void localDateObjectToJson() throws JsonProcessingException {
        JavaTime object = new JavaTime();
        object.setIsoLocalDate(LOCAL_DATE);
        String json = toJson(object);
        DocumentContext doc = toDoc(json);
        assertEquals("2018-01-01", doc.read("$.isoLocalDate", String.class));
    }

    @Test
    @DisplayName("LocalDate conversion json to object")
    public void localDateJsonToObject() throws IOException {
        String json = "{\"isoLocalDate\":\"2018-01-01\"}";
        JavaTime object = toObject(json, JavaTime.class);
        assertEquals(LOCAL_DATE, object.getIsoLocalDate());
    }

    @Test
    @DisplayName("LocalTime conversion object to json")
    public void localTimeObjectToJson() throws JsonProcessingException {
        JavaTime object = new JavaTime();
        object.setIsoLocalTime(LOCAL_TIME);
        String json = toJson(object);
        DocumentContext doc = toDoc(json);
        assertEquals("14:30:22.125", doc.read("$.isoLocalTime", String.class));
    }

    @Test
    @DisplayName("LocalTime conversion json to object")
    public void localTimeJsonToObject() throws IOException {
        String json = "{\"isoLocalTime\":\"14:30:22.125\"}";
        JavaTime object = toObject(json, JavaTime.class);
        assertEquals(LOCAL_TIME, object.getIsoLocalTime());
    }

    @Test
    @DisplayName("Year conversion object to json")
    public void yearObjectToJson() throws JsonProcessingException {
        JavaTime object = new JavaTime();
        object.setIsoYear(YEAR);
        String json = toJson(object);
        DocumentContext doc = toDoc(json);
        assertEquals("2018", doc.read("$.isoYear", String.class));
    }

    @Test
    @DisplayName("Year conversion json to object")
    public void yearJsonToObject() throws IOException {
        String json = "{\"isoYear\":\"2018\"}";
        JavaTime object = toObject(json, JavaTime.class);
        assertEquals(YEAR, object.getIsoYear());
    }

    @Test
    @DisplayName("YearMonth conversion object to json")
    public void yearMonthObjectToJson() throws JsonProcessingException {
        JavaTime object = new JavaTime();
        object.setIsoYearMonth(YEAR_MONTH);
        String json = toJson(object);
        DocumentContext doc = toDoc(json);
        assertEquals("2018-01", doc.read("$.isoYearMonth", String.class));
    }

    @Test
    @DisplayName("YearMonth conversion json to object")
    public void yearMonthJsonToObject() throws IOException {
        String json = "{\"isoYearMonth\":\"2018-01\"}";
        JavaTime object = toObject(json, JavaTime.class);
        assertEquals(YEAR_MONTH, object.getIsoYearMonth());
    }

    @Test
    @DisplayName("MonthDay conversion object to json")
    public void monthDayObjectToJson() throws JsonProcessingException {
        JavaTime object = new JavaTime();
        object.setIsoMonthDay(MONTH_DAY);
        String json = toJson(object);
        DocumentContext doc = toDoc(json);
        assertEquals("--01-01", doc.read("$.isoMonthDay", String.class));
    }

    @Test
    @DisplayName("MonthDay conversion json to object")
    public void monthDayJsonToObject() throws IOException {
        String json = "{\"isoMonthDay\":\"--01-01\"}";
        JavaTime object = toObject(json, JavaTime.class);
        assertEquals(MONTH_DAY, object.getIsoMonthDay());
    }

    @Test
    @DisplayName("ZonedDateTime conversion object to json")
    public void zonedDateTimeObjectToJson() throws JsonProcessingException {
        JavaTime object = new JavaTime();
        object.setIsoZonedDateTime(ZONED_DATE_TIME);
        String json = toJson(object);
        DocumentContext doc = toDoc(json);
        assertEquals("2018-01-01T14:30:22.125+01:00", doc.read("$.isoZonedDateTime", String.class));
    }

    @Test
    @DisplayName("ZonedDateTime conversion json to object")
    public void zonedDateTimeJsonToObject() throws IOException {
        String json = "{\"isoZonedDateTime\":\"2018-01-01T14:30:22.125+01:00\"}";
        JavaTime object = toObject(json, JavaTime.class);
        assertEquals(ZONED_DATE_TIME.withFixedOffsetZone(), object.getIsoZonedDateTime());
    }

    @Test
    @DisplayName("OffsetDateTime conversion object to json")
    public void offsetDateTimeObjectToJson() throws JsonProcessingException {
        JavaTime object = new JavaTime();
        object.setIsoOffsetDateTime(OFFSET_DATE_TIME);
        String json = toJson(object);
        DocumentContext doc = toDoc(json);
        assertEquals("2018-01-01T14:30:22.125+02:30", doc.read("$.isoOffsetDateTime", String.class));
    }

    @Test
    @DisplayName("OffsetDateTime conversion json to object")
    public void offsetDateTimeJsonToObject() throws IOException {
        String json = "{\"isoOffsetDateTime\":\"2018-01-01T14:30:22.125+02:30\"}";
        JavaTime object = toObject(json, JavaTime.class);
        assertEquals(OFFSET_DATE_TIME, object.getIsoOffsetDateTime());
    }

    @Test
    @DisplayName("OffsetTime conversion object to json")
    public void offsetTimeObjectToJson() throws JsonProcessingException {
        JavaTime object = new JavaTime();
        object.setIsoOffsetTime(OFFSET_TIME);
        String json = toJson(object);
        DocumentContext doc = toDoc(json);
        assertEquals("14:30:22.125+02:30", doc.read("$.isoOffsetTime", String.class));
    }

    @Test
    @DisplayName("OffsetTime conversion json to object")
    public void offsetTimeJsonToObject() throws IOException {
        String json = "{\"isoOffsetTime\":\"14:30:22.125+02:30\"}";
        JavaTime object = toObject(json, JavaTime.class);
        assertEquals(OFFSET_TIME, object.getIsoOffsetTime());
    }

    @Test
    @DisplayName("Instant conversion object to json")
    public void instantObjectToJson() throws JsonProcessingException {
        JavaTime object = new JavaTime();
        object.setIsoInstant(INSTANT);
        String json = toJson(object);
        DocumentContext doc = toDoc(json);
        assertEquals("2018-01-01T13:30:22.125Z", doc.read("$.isoInstant", String.class));
    }

    @Test
    @DisplayName("Instant conversion json to object")
    public void instantJsonToObject() throws IOException {
        String json = "{\"isoInstant\":\"2018-01-01T13:30:22.125Z\"}";
        JavaTime object = toObject(json, JavaTime.class);
        assertEquals(INSTANT, object.getIsoInstant());
    }

    @Test
    @DisplayName("Custom LocalDateTime conversion object to json")
    public void customLocalDateTimeObjectToJson() throws JsonProcessingException {
        JavaTime object = new JavaTime();
        object.setCustomLocalDateTime(LOCAL_DATE_TIME);
        String json = toJson(object);
        DocumentContext doc = toDoc(json);
        assertEquals("01/01/2018|14:30", doc.read("$.customLocalDateTime", String.class));
    }

    @Test
    @DisplayName("Custom LocalDateTime conversion json to object")
    public void customLocalDateTimeJsonToObject() throws IOException {
        String json = "{\"customLocalDateTime\":\"01/01/2018|14:30\"}";
        JavaTime object = toObject(json, JavaTime.class);
        assertEquals(LOCAL_DATE_TIME.truncatedTo(ChronoUnit.MINUTES), object.getCustomLocalDateTime());
    }

    @Test
    @DisplayName("Custom LocalDate conversion object to json")
    public void customLocalDateObjectToJson() throws JsonProcessingException {
        JavaTime object = new JavaTime();
        object.setCustomLocalDate(LOCAL_DATE);
        String json = toJson(object);
        DocumentContext doc = toDoc(json);
        assertEquals("01/01/2018", doc.read("$.customLocalDate", String.class));
    }

    @Test
    @DisplayName("Custom LocalDate conversion json to object")
    public void customLocalDateJsonToObject() throws IOException {
        String json = "{\"customLocalDate\":\"01/01/2018\"}";
        JavaTime object = toObject(json, JavaTime.class);
        assertEquals(LOCAL_DATE, object.getCustomLocalDate());
    }

    @Test
    @DisplayName("Custom LocalTime conversion object to json")
    public void customLocalTimeObjectToJson() throws JsonProcessingException {
        JavaTime object = new JavaTime();
        object.setCustomLocalTime(LOCAL_TIME);
        String json = toJson(object);
        DocumentContext doc = toDoc(json);
        assertEquals("14-30", doc.read("$.customLocalTime", String.class));
    }

    @Test
    @DisplayName("Custom LocalTime conversion json to object")
    public void customLocalTimeJsonToObject() throws IOException {
        String json = "{\"customLocalTime\":\"14-30\"}";
        JavaTime object = toObject(json, JavaTime.class);
        assertEquals(LOCAL_TIME.truncatedTo(ChronoUnit.MINUTES), object.getCustomLocalTime());
    }

    @Test
    @DisplayName("Custom Year conversion object to json")
    public void customYearObjectToJson() throws JsonProcessingException {
        JavaTime object = new JavaTime();
        object.setCustomYear(YEAR);
        String json = toJson(object);
        DocumentContext doc = toDoc(json);
        assertEquals("Y2018", doc.read("$.customYear", String.class));
    }

    @Test
    @DisplayName("Custom Year conversion json to object")
    public void customYearJsonToObject() throws IOException {
        String json = "{\"customYear\":\"Y2018\"}";
        JavaTime object = toObject(json, JavaTime.class);
        assertEquals(YEAR, object.getCustomYear());
    }

    @Test
    @DisplayName("Custom YearMonth conversion object to json")
    public void customYearMonthObjectToJson() throws JsonProcessingException {
        JavaTime object = new JavaTime();
        object.setCustomYearMonth(YEAR_MONTH);
        String json = toJson(object);
        DocumentContext doc = toDoc(json);
        assertEquals("2018:01", doc.read("$.customYearMonth", String.class));
    }

    @Test
    @DisplayName("Custom YearMonth conversion json to object")
    public void customYearMonthJsonToObject() throws IOException {
        String json = "{\"customYearMonth\":\"2018:01\"}";
        JavaTime object = toObject(json, JavaTime.class);
        assertEquals(YEAR_MONTH, object.getCustomYearMonth());
    }

    @Test
    @DisplayName("Custom MonthDay conversion object to json")
    public void customMonthDayObjectToJson() throws JsonProcessingException {
        JavaTime object = new JavaTime();
        object.setCustomMonthDay(MONTH_DAY);
        String json = toJson(object);
        DocumentContext doc = toDoc(json);
        assertEquals("01_01", doc.read("$.customMonthDay", String.class));
    }

    @Test
    @DisplayName("Custom MonthDay conversion json to object")
    public void customMonthDayJsonToObject() throws IOException {
        String json = "{\"customMonthDay\":\"01_01\"}";
        JavaTime object = toObject(json, JavaTime.class);
        assertEquals(MONTH_DAY, object.getCustomMonthDay());
    }

    @Test
    @DisplayName("Custom ZonedDateTime conversion object to json")
    public void customZonedDateTimeObjectToJson() throws JsonProcessingException {
        JavaTime object = new JavaTime();
        object.setCustomZonedDateTime(ZONED_DATE_TIME);
        String json = toJson(object);
        DocumentContext doc = toDoc(json);
        assertEquals("01/01/2018|14:30|+01:00", doc.read("$.customZonedDateTime", String.class));
    }

    @Test
    @DisplayName("Custom ZonedDateTime conversion json to object")
    public void customZonedDateTimeJsonToObject() throws IOException {
        String json = "{\"customZonedDateTime\":\"01/01/2018|14:30|+01:00\"}";
        JavaTime object = toObject(json, JavaTime.class);
        assertEquals(ZONED_DATE_TIME.truncatedTo(ChronoUnit.MINUTES).withFixedOffsetZone(),
                object.getCustomZonedDateTime());
    }

    @Test
    @DisplayName("Custom OffsetDateTime conversion object to json")
    public void customOffsetDateTimeObjectToJson() throws JsonProcessingException {
        JavaTime object = new JavaTime();
        object.setCustomOffsetDateTime(OFFSET_DATE_TIME);
        String json = toJson(object);
        DocumentContext doc = toDoc(json);
        assertEquals("01/01/2018|14:30|+02:30", doc.read("$.customOffsetDateTime", String.class));
    }

    @Test
    @DisplayName("Custom OffsetDateTime conversion json to object")
    public void customOffsetDateTimeJsonToObject() throws IOException {
        String json = "{\"customOffsetDateTime\":\"01/01/2018|14:30|+02:30\"}";
        JavaTime object = toObject(json, JavaTime.class);
        assertEquals(OFFSET_DATE_TIME.truncatedTo(ChronoUnit.MINUTES), object.getCustomOffsetDateTime());
    }

    @Test
    @DisplayName("Custom OffsetTime conversion object to json")
    public void customOffsetTimeObjectToJson() throws JsonProcessingException {
        JavaTime object = new JavaTime();
        object.setCustomOffsetTime(OFFSET_TIME);
        String json = toJson(object);
        DocumentContext doc = toDoc(json);
        assertEquals("14:30|+02:30", doc.read("$.customOffsetTime", String.class));
    }

    @Test
    @DisplayName("Custom OffsetTime conversion json to object")
    public void customOffsetTimeJsonToObject() throws IOException {
        String json = "{\"customOffsetTime\":\"14:30|+02:30\"}";
        JavaTime object = toObject(json, JavaTime.class);
        assertEquals(OFFSET_TIME.truncatedTo(ChronoUnit.MINUTES), object.getCustomOffsetTime());
    }

    @Test
    @DisplayName("Custom Instant conversion object to json")
    public void customInstantObjectToJson() throws JsonProcessingException {
        JavaTime object = new JavaTime();
        object.setCustomInstant(INSTANT);
        String json = toJson(object);
        DocumentContext doc = toDoc(json);
        assertEquals("01/01/2018|13:30|Z", doc.read("$.customInstant", String.class));
    }

    @Test
    @DisplayName("Custom Instant conversion json to object")
    public void customInstantJsonToObject() throws IOException {
        String json = "{\"customInstant\":\"01/01/2018|13:30|Z\"}";
        JavaTime object = toObject(json, JavaTime.class);
        assertEquals(INSTANT.truncatedTo(ChronoUnit.MINUTES), object.getCustomInstant());
    }
}
