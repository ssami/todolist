package org.example.storage;

import org.example.models.Todo;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class StorageGateway implements StorageInterface<Todo> {

    // should be a strategy thing that can be defined during startup
    Set<Todo> localTodoCache;

    public StorageGateway() {
        localTodoCache = new HashSet<>();
    }

    @Override
    public void store(Todo thingToStore) {
        localTodoCache.add(thingToStore);
    }

    @Override
    public Todo retrieve() {
        Predicate<Todo> contains = t -> t.thingToDo().contains("thing");
        return localTodoCache.stream().filter(contains).findFirst().get();
    }
}
