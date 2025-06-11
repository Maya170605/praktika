package com.example.customs.util;

public class UnpValidator {

    public static boolean isValid(String unp) {
        return unp != null && unp.matches("\\d{9}");
    }
}
