package org.example.storage;

import org.example.models.Todo;
import org.example.utils.Conversion;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DiskStorageGateway implements StorageInterface<Todo> {
    private final List<Todo> loadedToDoList;
    private final String originalFile;
    public DiskStorageGateway(String filePath) throws IOException {
        this.originalFile = filePath;
        Path desiredFile = Paths.get(originalFile);
        try {
            if (!Files.exists(desiredFile)) {
                // file doesn't exist, create
                // assumes this program has permissions
                Files.createFile(desiredFile);
                this.loadedToDoList = new ArrayList<>();
            } else {
                Stream<String> lines = Files.lines(desiredFile);
                List<String> todosList = lines.toList();
                this.loadedToDoList = todosList.stream()
                        .map(Conversion::parseString)
                        .collect(Collectors.toList());
                lines.close();
            }
        } catch (IOException e) {
            System.out.println(e);
            throw e;
        }
    }
    @Override
    public void store(Todo thingToStore) {
        this.loadedToDoList.add(thingToStore);
    }

    @Override
    public void remove(int index) {
        if (index >= 0 && index < this.loadedToDoList.size()){
            this.loadedToDoList.remove(index);
        } else {
          throw new IllegalArgumentException("Index of item to be removed is out of the range of todo items");
        }
    }

    @Override
    public List<Todo> retrieve(Predicate<Todo> strategy) {
        return this.loadedToDoList.stream()
                .filter(strategy)
                .collect(Collectors.toList());
    }

    @Override
    public Todo retrieveByIndex(int index) {
        if (this.loadedToDoList.size() < index - 1 || index < 0) {
            throw new IllegalArgumentException("Index requested is out of bounds");
        }
        return this.loadedToDoList.get(index);
    }

    @Override
    public void save() throws URISyntaxException, IOException {
        Path desiredFile = Paths.get(originalFile);
        StringBuffer buffer = new StringBuffer();
        this.loadedToDoList.forEach(t -> {
            var toWrite = t.toString() + "\n";
            buffer.append(toWrite);
        });
        Files.writeString(desiredFile, buffer.toString());
    }
}
