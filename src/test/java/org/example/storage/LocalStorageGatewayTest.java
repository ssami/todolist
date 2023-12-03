package org.example.storage;

import org.example.models.Priority;
import org.example.models.Todo;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

public class LocalStorageGatewayTest {

    private static LocalStorageGateway localStorageGateway;

    @BeforeClass
    public static void setup() {
        localStorageGateway = new LocalStorageGateway();
    }

    @Test
    public void testStorage_RetrieveByName() {
        String todoName = "test local gateway " + System.currentTimeMillis();
        Todo sample = new Todo(todoName, LocalDateTime.now(), Priority.HIGH);
        localStorageGateway.store(sample);
        List<Todo> retrievedTodo = localStorageGateway.retrieve(todo -> todo.thingToDo().equals(todoName));
        Assert.assertEquals(1, retrievedTodo.size());
        Assert.assertEquals(sample, retrievedTodo.get(0));
    }

    @Test
    public void testStorage_RetrieveByPriority() {
        Todo sample = new Todo("test local gateway " + System.currentTimeMillis(), LocalDateTime.now(), Priority.HIGH);
        Todo sample2 = new Todo("another test", LocalDateTime.now(), Priority.LOW);
        localStorageGateway.store(sample);
        localStorageGateway.store(sample2);
        List<Todo> retrievedTodo = localStorageGateway.retrieve(todo -> todo.priority().equals(Priority.LOW));
        Assert.assertEquals(1, retrievedTodo.size());
        Assert.assertEquals(sample2, retrievedTodo.get(0));
    }

    @Test
    public void testStorage_RetrieveByDate() {
        LocalDateTime localDateTime = LocalDateTime.of(2023, 7, 7, 10, 39);
        LocalDateTime localDateTimeCompare = LocalDateTime.of(2023, 7, 6, 0, 0);
        Todo sample = new Todo("test local gateway", localDateTime, Priority.HIGH);
        localStorageGateway.store(sample);
        List<Todo> retrievedTodo = localStorageGateway.retrieve(todo -> todo.dueBy().isAfter(localDateTimeCompare));
        Assert.assertEquals(1, retrievedTodo.size());
        Assert.assertEquals(sample, retrievedTodo.get(0));
    }

}
