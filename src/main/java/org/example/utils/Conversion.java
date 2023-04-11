package org.example.utils;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Conversion {
    public static LocalDateTime validDate(String dueBy) {
        Pattern p = Pattern.compile("(\\d{1,2})/(\\d{1,2})");
        Matcher m = p.matcher(dueBy);
        if (m.matches()) {
            int day = Integer.parseInt(m.group(1));
            int month = Integer.parseInt(m.group(2));
            return LocalDateTime.of(2023, Month.of(month), day, 0, 0);
        }
        else {
            throw new IllegalArgumentException("Date input must be in the format dd/mm");
        }
    }
}
