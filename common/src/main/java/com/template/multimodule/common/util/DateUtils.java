package com.template.multimodule.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private DateUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String format(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DEFAULT_FORMATTER);
    }

    public static String format(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDateTime parse(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(dateTimeStr, DEFAULT_FORMATTER);
    }

    public static LocalDateTime parse(String dateTimeStr, String pattern) {
        if (dateTimeStr == null || dateTimeStr.isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(pattern));
    }
}
