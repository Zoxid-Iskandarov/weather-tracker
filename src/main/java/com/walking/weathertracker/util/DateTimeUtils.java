package com.walking.weathertracker.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class DateTimeUtils {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static LocalDateTime parseToLocalDateTime(String date) {
        return LocalDateTime.parse(date, FORMATTER);
    }

    public static String formatToString(LocalDateTime localDateTime) {
        return localDateTime.format(FORMATTER);
    }
}
