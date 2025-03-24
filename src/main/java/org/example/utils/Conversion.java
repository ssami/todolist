package org.example.utils;

import org.example.models.Priority;
import org.example.models.Todo;


import java.time.LocalDateTime;
import java.time.Month;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Conversion {

    private static final String TODO_DELIMITER = ",";
    private static final Pattern p = Pattern.compile("(\\d{1,2})/(\\d{1,2})");

    public static LocalDateTime parseDate(String dueBy) {

        Matcher m = p.matcher(dueBy);
        if (m.matches()) {
            int month = Integer.parseInt(m.group(1));
            int day = Integer.parseInt(m.group(2));
            return LocalDateTime.of(2023, Month.of(month), day, 0, 0);
        }
        else {
            throw new IllegalArgumentException("Date input must be in the format dd/mm");
        }
    }

    public static Todo parseString(String inLine) {
        if (inLine.isEmpty()) {
            throw new IllegalArgumentException("Todo item cannot be empty");
        }
        var parts = inLine.split(TODO_DELIMITER);
        var todoStr = parts[0].trim();
        var dateObj = parts.length > 1 ? Conversion.parseDate(parts[1].trim()) : LocalDateTime.now();
        var priority = Priority.LOW;
        if (parts.length > 2) {
            try {
                priority = Priority.valueOf(parts[2].toUpperCase().trim());
            } catch (IllegalArgumentException e) {
                System.out.println("No such priority");
            }
        }
        return new Todo(todoStr, dateObj, priority);
    }
}
