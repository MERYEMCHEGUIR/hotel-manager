package com.hotel.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class DateUtils {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Parse date depuis string
     */
    public static LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr, FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * Formater date en string
     */
    public static String formatDate(LocalDate date) {
        if (date == null) return "";
        return date.format(FORMATTER);
    }

    /**
     * Calculer nombre de jours entre deux dates
     */
    public static long daysBetween(LocalDate start, LocalDate end) {
        if (start == null || end == null) return 0;
        return ChronoUnit.DAYS.between(start, end);
    }

    /**
     * Vérifier si date1 est avant date2
     */
    public static boolean isBefore(LocalDate date1, LocalDate date2) {
        if (date1 == null || date2 == null) return false;
        return date1.isBefore(date2);
    }

    /**
     * Vérifier si date est dans le futur
     */
    public static boolean isFuture(LocalDate date) {
        if (date == null) return false;
        return date.isAfter(LocalDate.now());
    }

    /**
     * Vérifier si date est dans le passé
     */
    public static boolean isPast(LocalDate date) {
        if (date == null) return false;
        return date.isBefore(LocalDate.now());
    }
}
