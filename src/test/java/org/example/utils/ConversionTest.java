package org.example.utils;

import org.example.models.Priority;
import org.example.models.Todo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.DateTimeException;

public class ConversionTest {

    @Test
    public void testParseDate() {
        LocalDateTime date = Conversion.parseDate("3/23");
        Assertions.assertEquals(Month.MARCH, date.getMonth());
        Assertions.assertEquals(23, date.getDayOfMonth());
    }

    @Test
    public void testParseDateInvalid() {
        Assertions.assertThrows(DateTimeException.class, () -> {
            Conversion.parseDate("13/23");
        });
    }

    @Test
    public void testParseDateInvalidFormat() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Conversion.parseDate("3-23");
        });
    }

    @Test
    public void testParseString() {
        String todoString = "this is a todo, 3/23, HIGH";
        Todo todo = Conversion.parseString(todoString);
        Assertions.assertEquals(new Todo("this is a todo", LocalDateTime.of(2023, 3, 23, 0, 0), Priority.HIGH), todo);
    }

    @Test
    public void testValidTodoSplitWithoutComma() {
        var expected = new Todo("test",
                LocalDateTime.of(2023, Month.of(3), 3, 0, 0),
                Priority.HIGH);
        Assertions.assertEquals(expected, Conversion.parseString("test, 3/3, HIGH"));
    }
}