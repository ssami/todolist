package org.example.storage;

import org.example.models.Todo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LocalStorageGateway implements StorageInterface<Todo> {

    // should be a strategy thing that can be defined during startup
    Set<Todo> localTodoCache;

    public LocalStorageGateway() {
        localTodoCache = new HashSet<>();
    }

    @Override
    public void store(Todo thingToStore) {
        localTodoCache.add(thingToStore);
    }

    @Override
    public List<Todo> retrieve(Predicate<Todo> strategy) {
        return localTodoCache
                .stream()
                .filter(strategy)
                .collect(Collectors.toList());
    }

    @Override
    public void save() {
        System.out.println(localTodoCache);
    }
}
