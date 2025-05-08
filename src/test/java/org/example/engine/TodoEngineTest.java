package org.example.engine;

import org.example.TodoEngine;
import org.example.models.Todo;
import org.example.storage.DiskStorageGateway;
import org.example.storage.LocalStorageGateway;
import org.example.storage.StorageInterface;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;

public class TodoEngineTest {

    @Test
    public void test_e2e_local(){
        LocalStorageGateway localStorageGateway = new LocalStorageGateway();
        TodoEngine testEngine = new TodoEngine(localStorageGateway);

        // test storage and retrieval
        testEngine.store("this is a todo0");
        List<Todo> undone = testEngine.retrieveUndoneList();
        Assertions.assertEquals(1, undone.size());
        Assertions.assertEquals("this is a todo0", undone.get(0).thingToDo());

        // test deletion and retrieval
        testEngine.store("this is a todo1");
        testEngine.store("this is a todo2");
        testEngine.remove(1); // will remove "this is a todo1
        List<Todo> undone2 = testEngine.retrieveUndoneList();
        Assertions.assertEquals(2, undone2.size());
        Assertions.assertEquals("this is a todo0", undone2.get(0).thingToDo());
        Assertions.assertEquals("this is a todo2", undone2.get(1).thingToDo());
    }

    @Test
    public void test_high_pri_local(){
        LocalStorageGateway localStorageGateway = new LocalStorageGateway();
        TodoEngine testEngine = new TodoEngine(localStorageGateway);

        // test storage and retrieval
        testEngine.store("this is a high priority todo0, 3/23, HIGH");
        List<Todo> high = testEngine.retrieveTopPriorityList();
        Assertions.assertEquals(1, high.size());
        Assertions.assertEquals("this is a high priority todo0", high.get(0).thingToDo());

        testEngine.store("this is a top3 todo0, 3/23, TOP3");
        List<Todo> top = testEngine.retrieveTopPriorityList();
        Assertions.assertEquals(2, top.size());
        Assertions.assertEquals("this is a top3 todo0", top.get(0).thingToDo());
        Assertions.assertEquals("this is a high priority todo0", top.get(1).thingToDo());
    }

    @Test
    public void test_e2e_storage() throws URISyntaxException, IOException {
        String filename = "/tmp/test_todos_" + System.currentTimeMillis() + ".txt";
        StorageInterface<Todo> diskStorage = new DiskStorageGateway(filename);
        TodoEngine testEngine = new TodoEngine(diskStorage);

        // test storage and retrieval
        testEngine.store("this is a todo0");
        List<Todo> undone = testEngine.retrieveUndoneList();
        Assertions.assertEquals(1, undone.size());
        Assertions.assertEquals("this is a todo0", undone.get(0).thingToDo());

        // test deletion and retrieval
        testEngine.store("this is a todo1");
        testEngine.store("this is a todo2");
        testEngine.remove(1); // will remove "this is a todo1"
        List<Todo> undone2 = testEngine.retrieveUndoneList();
        Assertions.assertEquals(2, undone2.size());
        Assertions.assertEquals("this is a todo0", undone2.get(0).thingToDo());
        Assertions.assertEquals("this is a todo2", undone2.get(1).thingToDo());

        testEngine.save();

        // test retrieval from same file
        StorageInterface<Todo> diskStorage2 = new DiskStorageGateway(filename);
        TodoEngine testEngine2 = new TodoEngine(diskStorage2);
        List<Todo> undone3 = testEngine2.retrieveUndoneList();
        Assertions.assertEquals(2, undone2.size());
        Assertions.assertEquals("this is a todo0", undone3.get(0).thingToDo());
        Assertions.assertEquals("this is a todo2", undone3.get(1).thingToDo());

    }
}
