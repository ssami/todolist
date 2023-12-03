package org.example.utils;

import org.example.models.Priority;
import org.example.models.Todo;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class ConversionTest {

    @Test
    public void test_validDayMonth() {
        LocalDateTime convertedDate = Conversion.parseDate("12/1");
        assertEquals(Month.DECEMBER, convertedDate.getMonth());
        assertEquals(1, convertedDate.getDayOfMonth());
    }

    @Test
    public void test_invalidPattern() {
        assertThrows(
                IllegalArgumentException.class,
                () -> Conversion.parseDate("1-1")
        );
    }

    @Test
    public void test_invalidDayMonth() {
        assertThrows(
                RuntimeException.class,
                () -> Conversion.parseDate("13/25")
        );
    }

    @Test
    public void test_validTdodoSplitWithoutComma() {
        var expected = new Todo("test",
                LocalDateTime.of(2023, Month.of(3), 3, 0, 0),
                Priority.HIGH);
        assertEquals(expected, Conversion.parseString("test, 3/3, HIGH"));
    }
}