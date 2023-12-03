package org.example;

import org.example.models.Todo;
import org.example.storage.StorageInterface;
import org.example.utils.Conversion;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;

public class TodoEngine {

    private final StorageInterface<Todo> todoStorageGateway;
    public TodoEngine(StorageInterface<Todo> storageGateway) {
        this.todoStorageGateway = storageGateway;
    }

    public void store(String rawInput) {
        var todo = Conversion.parseString(rawInput);
        this.todoStorageGateway.store(todo);
    }

    public List<Todo> retrieveTodayList() {
        LocalDateTime today = LocalDateTime.now();
        Predicate<Todo> findDueToday = t -> {
            LocalDateTime todoDue = t.dueBy();
            return todoDue.getMonth().equals(today.getMonth())
                    && todoDue.getDayOfMonth() == today.getDayOfMonth();
        };
        return this.todoStorageGateway.retrieve(findDueToday);
    }

    public List<Todo> retrieveByDate(String dateTime){
        throw new UnsupportedOperationException("not implemented yet");
    }

    public void save() throws URISyntaxException, IOException {
        this.todoStorageGateway.save();
    }
}
