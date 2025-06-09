package com.example.customs.util;

public class UnpValidator {

    public static boolean isValid(String unp) {
        if (!unp.matches("\\d{9}")) return false;

        int[] weights = {29, 23, 19, 17, 13, 7, 5, 3};
        int sum = 0;
        for (int i = 0; i < 8; i++) {
            sum += Character.getNumericValue(unp.charAt(i)) * weights[i];
        }
        int control = sum % 11;
        if (control == 10) control = 0;

        return control == Character.getNumericValue(unp.charAt(8));
    }
}
