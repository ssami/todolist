package org.example.storage;

import org.example.models.Todo;
import org.example.utils.Conversion;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DiskStorageGateway implements StorageInterface<Todo> {
    private final List<Todo> loadedToDoList;
    private final String originalFile;
    public DiskStorageGateway(String filePath) throws URISyntaxException, IOException {
        originalFile = filePath;
        Path desiredFile = Paths.get(originalFile);
        if (!Files.exists(desiredFile)) {
            // file doesn't exist, create
            // assumes this program has permissions
            Files.createFile(desiredFile);
            loadedToDoList = new ArrayList<>();
        } else {
            Stream<String> lines = Files.lines(desiredFile);
            List<String> todosList = lines.toList();
            loadedToDoList = todosList.stream()
                    .map(Conversion::parseString)
                    .collect(Collectors.toList());
            lines.close();
        }

    }
    @Override
    public void store(Todo thingToStore) {
        this.loadedToDoList.add(thingToStore);
    }

    @Override
    public List<Todo> retrieve(Predicate<Todo> strategy) {
        return null;
    }

    @Override
    public void save() throws URISyntaxException, IOException {
        Path desiredFile = Paths.get(originalFile);
        loadedToDoList.forEach(t -> {
                        try {
                            Files.write(desiredFile, t.toString().getBytes(), StandardOpenOption.APPEND);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });

    }
}