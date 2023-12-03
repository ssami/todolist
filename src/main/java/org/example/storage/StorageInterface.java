package org.example.storage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.function.Predicate;

public interface StorageInterface<T> {

    void store(T thingToStore);
    List<T> retrieve(Predicate<T> strategy);
    void save() throws URISyntaxException, IOException;

}
