package com.github.supercodingproject2mall.order.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static LocalDateTime parseLocalDateTime(String dateString) {
        return LocalDateTime.parse(dateString, INPUT_FORMATTER);
    }

    public static String formatLocalDateTime(LocalDateTime dateTime) {
        return dateTime.format(OUTPUT_FORMATTER);
    }
}
