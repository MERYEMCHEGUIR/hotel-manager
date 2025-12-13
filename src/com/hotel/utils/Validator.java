package com.hotel.utils;

public class Validator {
    public static boolean isEmailValid(String email) {
        return email.contains("@") && email.contains(".");
    }
}
