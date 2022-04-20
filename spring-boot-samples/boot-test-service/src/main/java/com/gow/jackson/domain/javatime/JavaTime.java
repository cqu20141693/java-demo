package com.gow.jackson.domain.javatime;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;


/**
 * @author gow
 * @date 2021/7/24
 */

public class JavaTime {

    private LocalDate isoLocalDate;
    private LocalDateTime isoLocalDateTime;
    private LocalTime isoLocalTime;
    private Year isoYear;
    private YearMonth isoYearMonth;
    private MonthDay isoMonthDay;
    private ZonedDateTime isoZonedDateTime;
    private OffsetDateTime isoOffsetDateTime;
    private OffsetTime isoOffsetTime;
    private Instant isoInstant;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate customLocalDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy|HH:mm")
    private LocalDateTime customLocalDateTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH-mm")
    private LocalTime customLocalTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "'Y'yyyy")
    private Year customYear;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy:MM")
    private YearMonth customYearMonth;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM_dd")
    private MonthDay customMonthDay;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy|HH:mm|XXX")
    private ZonedDateTime customZonedDateTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy|HH:mm|XXX")
    private OffsetDateTime customOffsetDateTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm|XXX")
    private OffsetTime customOffsetTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy|HH:mm|XXX", timezone = "UTC")
    private Instant customInstant;

    public JavaTime() {
    }

    public LocalDate getIsoLocalDate() {
        return isoLocalDate;
    }

    public void setIsoLocalDate(LocalDate isoLocalDate) {
        this.isoLocalDate = isoLocalDate;
    }

    public LocalDateTime getIsoLocalDateTime() {
        return isoLocalDateTime;
    }

    public void setIsoLocalDateTime(LocalDateTime isoLocalDateTime) {
        this.isoLocalDateTime = isoLocalDateTime;
    }

    public LocalTime getIsoLocalTime() {
        return isoLocalTime;
    }

    public void setIsoLocalTime(LocalTime isoLocalTime) {
        this.isoLocalTime = isoLocalTime;
    }

    public Year getIsoYear() {
        return isoYear;
    }

    public void setIsoYear(Year isoYear) {
        this.isoYear = isoYear;
    }

    public YearMonth getIsoYearMonth() {
        return isoYearMonth;
    }

    public void setIsoYearMonth(YearMonth isoYearMonth) {
        this.isoYearMonth = isoYearMonth;
    }

    public MonthDay getIsoMonthDay() {
        return isoMonthDay;
    }

    public void setIsoMonthDay(MonthDay isoMonthDay) {
        this.isoMonthDay = isoMonthDay;
    }

    public ZonedDateTime getIsoZonedDateTime() {
        return isoZonedDateTime;
    }

    public void setIsoZonedDateTime(ZonedDateTime isoZonedDateTime) {
        this.isoZonedDateTime = isoZonedDateTime;
    }

    public OffsetDateTime getIsoOffsetDateTime() {
        return isoOffsetDateTime;
    }

    public void setIsoOffsetDateTime(OffsetDateTime isoOffsetDateTime) {
        this.isoOffsetDateTime = isoOffsetDateTime;
    }

    public OffsetTime getIsoOffsetTime() {
        return isoOffsetTime;
    }

    public void setIsoOffsetTime(OffsetTime isoOffsetTime) {
        this.isoOffsetTime = isoOffsetTime;
    }

    public Instant getIsoInstant() {
        return isoInstant;
    }

    public void setIsoInstant(Instant isoInstant) {
        this.isoInstant = isoInstant;
    }

    public LocalDate getCustomLocalDate() {
        return customLocalDate;
    }

    public void setCustomLocalDate(LocalDate customLocalDate) {
        this.customLocalDate = customLocalDate;
    }

    public LocalDateTime getCustomLocalDateTime() {
        return customLocalDateTime;
    }

    public void setCustomLocalDateTime(LocalDateTime customLocalDateTime) {
        this.customLocalDateTime = customLocalDateTime;
    }

    public LocalTime getCustomLocalTime() {
        return customLocalTime;
    }

    public void setCustomLocalTime(LocalTime customLocalTime) {
        this.customLocalTime = customLocalTime;
    }

    public Year getCustomYear() {
        return customYear;
    }

    public void setCustomYear(Year customYear) {
        this.customYear = customYear;
    }

    public YearMonth getCustomYearMonth() {
        return customYearMonth;
    }

    public void setCustomYearMonth(YearMonth customYearMonth) {
        this.customYearMonth = customYearMonth;
    }

    public MonthDay getCustomMonthDay() {
        return customMonthDay;
    }

    public void setCustomMonthDay(MonthDay customMonthDay) {
        this.customMonthDay = customMonthDay;
    }

    public ZonedDateTime getCustomZonedDateTime() {
        return customZonedDateTime;
    }

    public void setCustomZonedDateTime(ZonedDateTime customZonedDateTime) {
        this.customZonedDateTime = customZonedDateTime;
    }

    public OffsetDateTime getCustomOffsetDateTime() {
        return customOffsetDateTime;
    }

    public void setCustomOffsetDateTime(OffsetDateTime customOffsetDateTime) {
        this.customOffsetDateTime = customOffsetDateTime;
    }

    public OffsetTime getCustomOffsetTime() {
        return customOffsetTime;
    }

    public void setCustomOffsetTime(OffsetTime customOffsetTime) {
        this.customOffsetTime = customOffsetTime;
    }

    public Instant getCustomInstant() {
        return customInstant;
    }

    public void setCustomInstant(Instant customInstant) {
        this.customInstant = customInstant;
    }
}