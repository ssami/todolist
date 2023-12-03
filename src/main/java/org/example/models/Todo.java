package org.example.models;

import java.time.LocalDateTime;

public record Todo(String thingToDo, LocalDateTime dueBy, Priority priority) {

    public Todo {
        if (thingToDo.isBlank()) {
            throw new IllegalArgumentException("Todo string cannot be null!");
        }
    }

    @Override
    public String toString() {
        return this.thingToDo + "," +
                this.dueBy.getDayOfMonth() + "/" + this.dueBy.getMonthValue() + "," +
                this.priority.toString();
    }
}