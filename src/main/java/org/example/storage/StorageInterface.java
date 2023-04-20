package org.example.storage;

import java.util.List;
import java.util.function.Predicate;

interface StorageInterface<T> {

    void store(T thingToStore);
    List<T> retrieve(Predicate<T> strategy);
    void save();

}
