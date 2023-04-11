package org.example.storage;

interface StorageInterface<T> {

    void store(T thingToStore);
    T retrieve();

}
