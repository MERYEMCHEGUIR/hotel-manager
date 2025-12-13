package com.hotel.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Utilitaire pour gérer le formatage et le parsing des dates.
 */
public class DateUtils {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Convertit une chaîne de caractères (JJ/MM/AAAA) en objet LocalDate.
     * @param dateString La date au format texte.
     * @return L'objet LocalDate correspondant.
     * @throws DateTimeParseException Si le format n'est pas valide.
     */
    public static LocalDate parseDate(String dateString) throws DateTimeParseException {
        return LocalDate.parse(dateString, DATE_FORMATTER);
    }

    /**
     * Convertit un objet LocalDate en chaîne de caractères (JJ/MM/AAAA).
     * @param date L'objet LocalDate.
     * @return La chaîne de caractères formatée.
     */
    public static String formatDate(LocalDate date) {
        return date.format(DATE_FORMATTER);
    }
}