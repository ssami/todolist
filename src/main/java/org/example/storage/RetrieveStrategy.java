package org.example.storage;

import java.util.function.Predicate;

public record RetrieveStrategy<T> (Predicate<T> strategy) {

}
