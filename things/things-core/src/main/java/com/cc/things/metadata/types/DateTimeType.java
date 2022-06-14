package com.cc.things.metadata.types;

import com.cc.util.StringUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 时间默认使用时间戳，可以自定义format
 */
@Getter
@Setter
@Slf4j
public class DateTimeType extends AbstractType<DateTimeType> implements DataType, Converter<Date> {
    public static final String ID = "date";

    public static final String TIMESTAMP_FORMAT = "timestamp";

    public static final DateTimeType GLOBAL = new DateTimeType();

    private String format = TIMESTAMP_FORMAT;

    private ZoneId zoneId = ZoneId.systemDefault();

    private DateTimeFormatter formatter;


    public DateTimeType timeZone(ZoneId zoneId) {
        this.zoneId = zoneId;

        return this;
    }

    public DateTimeType format(String format) {
        this.format = format;
        this.getFormatter();
        return this;
    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "时间";
    }

    protected DateTimeFormatter getFormatter() {
        if (formatter == null && !TIMESTAMP_FORMAT.equals(format)) {
            formatter = DateTimeFormatter.ofPattern(format);
        }
        return formatter;
    }

    @Override
    public ValidateResult validate(Object value) {
        if ((value = convert(value)) == null) {
            return ValidateResult.fail("不是合法的时间格式");
        }
        return ValidateResult.success(value);
    }

    @Override
    public String format(Object value) {
        try {
            if (TIMESTAMP_FORMAT.equals(format)) {
                return String.valueOf(convert(value).getTime());
            }
            Date dateValue = convert(value);
            if (dateValue == null) {
                return "";
            }
            return LocalDateTime
                    .ofInstant(dateValue.toInstant(), zoneId)
                    .format(getFormatter());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return "";
    }

    public Date convert(Object value) {

        if (value instanceof Instant) {
            return Date.from(((Instant) value));
        }
        if (value instanceof LocalDateTime) {
            return Date.from(((LocalDateTime) value).atZone(zoneId).toInstant());

        }

        if (value instanceof Date) {
            return ((Date) value);
        }
        if (value instanceof Number) {
            return new Date(((Number) value).longValue());
        }
        if (value instanceof String) {
            if (StringUtils.isNumber(value)) {
                return new Date(Long.parseLong((String) value));
            }
            // 可以提前定义格式化器

            // 使用默认格式化器
            DateTimeFormatter formatter = getFormatter();
            if (null == formatter) {
                throw new IllegalArgumentException("unsupported date format:" + value);
            }
            return Date.from(LocalDateTime.parse(((String) value), formatter)
                    .atZone(zoneId)
                    .toInstant());
        }
        throw new IllegalArgumentException("can not format datetime :" + value);
    }


}
