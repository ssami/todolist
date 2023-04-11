package org.example.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;

class ConversionTest {

    @Test
    public void test_validDayMonth() {
        LocalDateTime convertedDate = Conversion.validDate("1/12");
        Assertions.assertEquals(Month.DECEMBER, convertedDate.getMonth());
        Assertions.assertEquals(1, convertedDate.getDayOfMonth());
    }

    @Test
    public void test_invalidPattern() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> Conversion.validDate("1-1")
        );
    }

    @Test
    public void test_invalidDayMonth() {
        Assertions.assertThrows(
                RuntimeException.class,
                () -> Conversion.validDate("1/13")
        );
    }
}