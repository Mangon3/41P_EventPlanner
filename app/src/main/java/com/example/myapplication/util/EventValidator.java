package com.example.myapplication.util;

import java.util.Calendar;

public class EventValidator {
    public static boolean isValid(String title, long dateInMillis) {
        if (title == null || title.trim().isEmpty()) {
            return false;
        }

        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        if (dateInMillis < today.getTimeInMillis()) {
            return false;
        }

        return true;
    }
}
