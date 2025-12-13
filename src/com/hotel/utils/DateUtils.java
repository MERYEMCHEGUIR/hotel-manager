package com.hotel.utils;

import java.time.LocalDate;

public class DateUtils {
    public static boolean isValidDateRange(LocalDate start, LocalDate end) {
        return start.isBefore(end);
    }
}
