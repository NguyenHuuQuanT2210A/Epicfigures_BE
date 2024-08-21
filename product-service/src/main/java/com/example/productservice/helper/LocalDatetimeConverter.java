package com.example.productservice.helper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDatetimeConverter {
    public static LocalDateTime toLocalDateTime(String dateStr, boolean startDate) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDate getDate = LocalDate.parse(dateStr, dateFormatter);
        if (startDate) {
            dateStr += " 00:00:00";
            LocalDateTime startDateTime = LocalDateTime.parse(dateStr, dateTimeFormatter);
            return startDateTime;
        }else {
            dateStr += " 23:59:59";
            LocalDateTime endDateTime = LocalDateTime.parse(dateStr, dateTimeFormatter);
            return endDateTime;
        }
    }
}
