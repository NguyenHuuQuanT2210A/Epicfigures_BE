package com.example.inventoryservice.helper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDatetimeConverter {
    public static LocalDateTime toLocalDateTime(String dateTimeStr) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateTimeStr, dateTimeFormatter);
    }

    public static String convertLocalDateTimeToString(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }
}
