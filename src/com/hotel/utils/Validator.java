package com.hotel.utils;

import java.util.regex.Pattern;

public class Validator {

    // Pattern pour email professionnel
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    // Pattern pour téléphone marocain
    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^(\\+212|0)[5-7][0-9]{8}$");

    // Pattern pour CIN marocain
    private static final Pattern CIN_PATTERN =
            Pattern.compile("^[A-Z]{1,2}[0-9]{5,7}$");

    /**
     * Valider email
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Valider numéro de téléphone
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    /**
     * Valider CIN marocain
     */
    public static boolean isValidCIN(String cin) {
        if (cin == null || cin.trim().isEmpty()) {
            return false;
        }
        return CIN_PATTERN.matcher(cin.toUpperCase().trim()).matches();
    }

    /**
     * Valider que le string n'est pas vide
     */
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * Valider nombre positif
     */
    public static boolean isPositive(double value) {
        return value > 0;
    }
}
