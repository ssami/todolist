package org.example.models;

import java.time.LocalDateTime;
import java.util.Objects;

public record Todo(String thingToDo, LocalDateTime dueBy, Priority priority, boolean isCompleted, LocalDateTime completedAt) {

    public Todo {
        Objects.requireNonNull(thingToDo, "Todo description cannot be null");
        if (thingToDo.isBlank()) {
            throw new IllegalArgumentException("Todo description cannot be blank!");
        }
    }

    public Todo(String thingToDo, LocalDateTime dueBy, Priority priority) {
        this(thingToDo, dueBy, priority, false, null);
    }

    public Todo copyWith(String thingToDo, LocalDateTime dueBy, Priority priority, Boolean isCompleted, LocalDateTime completedAt) {
        String newThingToDo = thingToDo != null ? thingToDo : this.thingToDo;
        if (thingToDo != null) {
            Objects.requireNonNull(thingToDo, "Todo description cannot be null");
            if (thingToDo.isBlank()) {
                throw new IllegalArgumentException("Todo description cannot be blank!");
            }
        }

        return new Todo(
            newThingToDo,
            dueBy != null ? dueBy : this.dueBy,
            priority != null ? priority : this.priority,
            isCompleted != null ? isCompleted : this.isCompleted,
            completedAt != null ? completedAt : this.completedAt
        );
    }

    @Override
    public String toString() {
        String baseString = this.thingToDo + "," +
                this.dueBy.getMonthValue() + "/" + this.dueBy.getDayOfMonth() + "," +
                this.priority.toString();
        
        if (this.isCompleted) {
            return baseString + "," + this.isCompleted + "," + this.completedAt;
        }
        return baseString;
    }
}