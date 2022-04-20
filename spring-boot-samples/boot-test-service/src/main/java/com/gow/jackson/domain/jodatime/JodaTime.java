package com.gow.jackson.domain.jodatime;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.joda.time.DateTime;
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

public class JodaTime {
    private DateTime isoDateTime;
    private Instant isoInstant;
    private YearMonth isoYearMonth;
    private MonthDay isoMonthDay;
    private LocalDateTime isoLocalDateTime;
    private LocalDate isoLocalDate;
    private LocalTime isoLocalTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH_mm_ss_SSS|z")
    private DateTime customDateTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH_mm_ss_SSS|z")
    private Instant customInstant;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM")
    private YearMonth customYearMonth;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM_dd")
    private MonthDay customMonthDay;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH_mm_ss_SSS")
    private LocalDateTime customLocalDateTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate customLocalDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH_mm_ss_SSS")
    private LocalTime customLocalTime;

    public JodaTime() {
    }

    public DateTime getIsoDateTime() {
        return isoDateTime;
    }

    public void setIsoDateTime(DateTime isoDateTime) {
        this.isoDateTime = isoDateTime;
    }

    public Instant getIsoInstant() {
        return isoInstant;
    }

    public void setIsoInstant(Instant isoInstant) {
        this.isoInstant = isoInstant;
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

    public LocalDateTime getIsoLocalDateTime() {
        return isoLocalDateTime;
    }

    public void setIsoLocalDateTime(LocalDateTime isoLocalDateTime) {
        this.isoLocalDateTime = isoLocalDateTime;
    }

    public LocalDate getIsoLocalDate() {
        return isoLocalDate;
    }

    public void setIsoLocalDate(LocalDate isoLocalDate) {
        this.isoLocalDate = isoLocalDate;
    }

    public LocalTime getIsoLocalTime() {
        return isoLocalTime;
    }

    public void setIsoLocalTime(LocalTime isoLocalTime) {
        this.isoLocalTime = isoLocalTime;
    }

    public DateTime getCustomDateTime() {
        return customDateTime;
    }

    public void setCustomDateTime(DateTime customDateTime) {
        this.customDateTime = customDateTime;
    }

    public Instant getCustomInstant() {
        return customInstant;
    }

    public void setCustomInstant(Instant customInstant) {
        this.customInstant = customInstant;
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

    public LocalDateTime getCustomLocalDateTime() {
        return customLocalDateTime;
    }

    public void setCustomLocalDateTime(LocalDateTime customLocalDateTime) {
        this.customLocalDateTime = customLocalDateTime;
    }

    public LocalDate getCustomLocalDate() {
        return customLocalDate;
    }

    public void setCustomLocalDate(LocalDate customLocalDate) {
        this.customLocalDate = customLocalDate;
    }

    public LocalTime getCustomLocalTime() {
        return customLocalTime;
    }

    public void setCustomLocalTime(LocalTime customLocalTime) {
        this.customLocalTime = customLocalTime;
    }
}