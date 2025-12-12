package com.example.passwordManager.Utils;

public class InputUtils {
    public static Integer parseIntOrNull(String s) {
        try {
            return Integer.valueOf(s.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static int parseIntOrThrow(String s) {
        return Integer.parseInt(s.trim());
    }
}
