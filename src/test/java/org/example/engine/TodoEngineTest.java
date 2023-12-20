package org.example.engine;

import org.example.TodoEngine;
import org.example.models.Todo;
import org.example.storage.DiskStorageGateway;
import org.example.storage.LocalStorageGateway;
import org.example.storage.StorageInterface;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class TodoEngineTest {

    @Test
    public void test_e2e_local(){
        LocalStorageGateway localStorageGateway = new LocalStorageGateway();
        TodoEngine testEngine = new TodoEngine(localStorageGateway);

        // test storage and retrieval
        testEngine.store("this is a todo0");
        List<Todo> undone = testEngine.retrieveUndoneList();
        Assert.assertEquals(1, undone.size());
        Assert.assertEquals("this is a todo0", undone.get(0).thingToDo());

        // test deletion and retrieval
        testEngine.store("this is a todo1");
        testEngine.store("this is a todo2");
        testEngine.remove(1); // will remove "this is a todo1
        List<Todo> undone2 = testEngine.retrieveUndoneList();
        Assert.assertEquals(2, undone2.size());
        Assert.assertEquals("this is a todo0", undone2.get(0).thingToDo());
        Assert.assertEquals("this is a todo2", undone2.get(1).thingToDo());
    }

    @Test
    public void test_e2e_storage() throws URISyntaxException, IOException {
        String filename = "/tmp/test_todos_" + System.currentTimeMillis() + ".txt";
        StorageInterface<Todo> diskStorage = new DiskStorageGateway(filename);
        TodoEngine testEngine = new TodoEngine(diskStorage);

        // test storage and retrieval
        testEngine.store("this is a todo0");
        List<Todo> undone = testEngine.retrieveUndoneList();
        Assert.assertEquals(1, undone.size());
        Assert.assertEquals("this is a todo0", undone.get(0).thingToDo());

        // test deletion and retrieval
        testEngine.store("this is a todo1");
        testEngine.store("this is a todo2");
        testEngine.remove(1); // will remove "this is a todo1
        List<Todo> undone2 = testEngine.retrieveUndoneList();
        Assert.assertEquals(2, undone2.size());
        Assert.assertEquals("this is a todo0", undone2.get(0).thingToDo());
        Assert.assertEquals("this is a todo2", undone2.get(1).thingToDo());

        testEngine.save();

        // test retrieval from same file
        StorageInterface<Todo> diskStorage2 = new DiskStorageGateway(filename);
        TodoEngine testEngine2 = new TodoEngine(diskStorage2);
        List<Todo> undone3 = testEngine2.retrieveUndoneList();
        Assert.assertEquals(2, undone2.size());
        Assert.assertEquals("this is a todo0", undone2.get(0).thingToDo());
        Assert.assertEquals("this is a todo2", undone2.get(1).thingToDo());

    }
}
