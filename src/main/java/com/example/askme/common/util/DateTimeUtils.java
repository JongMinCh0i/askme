package com.example.askme.common.util;

import java.time.LocalDateTime;
import java.util.Date;

public class DateTimeUtils {

    public static LocalDateTime convertToLocalDateTime(Date date) {
        return date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
    }

}
