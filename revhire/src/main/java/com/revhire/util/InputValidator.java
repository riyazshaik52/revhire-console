package com.revhire.util;

import java.util.regex.Pattern;

public class InputValidator {

    private static final String EMAIL_REGEX =
            "^[A-Za-z0-9+_.-]+@(.+)$";

    public static boolean isValidEmail(String email) {
        return Pattern.matches(EMAIL_REGEX, email);
    }

    public static boolean isValidPassword(String password) {
        return password.length() >= 6;
    }
}
