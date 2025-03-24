package org.example;

import org.example.models.Priority;
import org.example.models.Todo;
import org.example.storage.StorageInterface;
import org.example.utils.Conversion;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class TodoEngine {

    private StorageInterface<Todo> todoStorageGateway;

    public TodoEngine() {
        this.todoStorageGateway = null;
    }

    public TodoEngine(StorageInterface<Todo> storageGateway) {
        this.todoStorageGateway = storageGateway;
    }

    public void setTodoStorageGateway(StorageInterface<Todo> storageGateway) {
        this.todoStorageGateway = storageGateway;
    }

    public void store(String rawInput) {
        var todo = Conversion.parseString(rawInput);
        if (null == this.todoStorageGateway) {
            throw new IllegalStateException("No storage location listed");
        }
        this.todoStorageGateway.store(todo);
    }

    public List<Todo> retrieveUndoneList() {
        // TODO: allow seeing "completed" items by including that as a field in Todo
        return this.todoStorageGateway.retrieve(t -> true);
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

    public List<Todo> retrieveTopPriorityList() {
        Predicate<Todo> findTop3Priority = t -> t.priority().equals(Priority.TOP3);
        Predicate<Todo> findHighPriority = t -> t.priority().equals(Priority.HIGH);
        List<Todo> top3 = this.todoStorageGateway.retrieve(findTop3Priority);
        List<Todo> high = this.todoStorageGateway.retrieve(findHighPriority);

        List<Todo> topPriorityList = new ArrayList<>(top3);
        topPriorityList.addAll(high);

        return topPriorityList;
    };


    public void remove(int index) {
        this.todoStorageGateway.remove(index);
    }

    public List<Todo> retrieveByDate(String dateTime){
        throw new UnsupportedOperationException("not implemented yet");
    }

    public void save() throws URISyntaxException, IOException {
        this.todoStorageGateway.save();
    }
}
