package org.example.models;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;

public class TodoTest {
    @Test
    public void testEquality() {
        // how does record equality work? https://tinyurl.com/5n864xxe
        String todo = "test thing to do";
        LocalDateTime localDateTime = LocalDateTime.of(2023, 7, 7, 10, 39);
        Todo t1 = new Todo(todo, localDateTime, Priority.HIGH);
        Todo t2 = new Todo(todo, localDateTime, Priority.HIGH);
        Assert.assertEquals(t1, t2);
    }

    @Test
    public void testNonEquality_Todo() {
        // how does record equality work? https://tinyurl.com/5n864xxe
        String todo = "test thing to do";
        LocalDateTime localDateTime = LocalDateTime.of(2023, 7, 7, 10, 39);
        Todo t1 = new Todo(todo, localDateTime, Priority.HIGH);
        Todo t2 = new Todo(todo + "!", localDateTime, Priority.HIGH);
        Assert.assertNotEquals(t1, t2);
    }

    @Test
    public void testNonEquality_Time() {
        // how does record equality work? https://tinyurl.com/5n864xxe
        String todo = "test thing to do!";
        LocalDateTime localDateTime1 = LocalDateTime.of(2023, 7, 7, 10, 39);
        LocalDateTime localDateTime2 = LocalDateTime.of(2023, 7, 7, 10, 38);
        Todo t1 = new Todo(todo, localDateTime1, Priority.HIGH);
        Todo t2 = new Todo(todo, localDateTime2, Priority.HIGH);
        Assert.assertNotEquals(t1, t2);
    }

    @Test
    public void testNonEquality_Priority() {
        // how does record equality work? https://tinyurl.com/5n864xxe
        String todo = "test thing to do!";
        LocalDateTime localDateTime = LocalDateTime.of(2023, 7, 7, 10, 39);
        Todo t1 = new Todo(todo, localDateTime, Priority.HIGH);
        Todo t2 = new Todo(todo, localDateTime, Priority.LOW);
        Assert.assertNotEquals(t1, t2);
    }
}
